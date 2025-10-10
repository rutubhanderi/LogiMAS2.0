package com.logistics.shipment_tracker.service;

import com.logistics.shipment_tracker.dto.ShipmentDto;
import com.logistics.shipment_tracker.dto.ShipmentTrackingDto;
import com.logistics.shipment_tracker.exception.ResourceNotFoundException;
import com.logistics.shipment_tracker.model.*;
import com.logistics.shipment_tracker.repository.ShipmentLogRepository;
import com.logistics.shipment_tracker.repository.ShipmentRepository;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipmentLogRepository shipmentLogRepository;

    public Shipment createShipment(ShipmentDto shipmentDto, Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Shipment shipment = new Shipment();
        shipment.setCustomer(customer);
        shipment.setOrigin(shipmentDto.getOrigin());
        shipment.setDestination(shipmentDto.getDestination());
        shipment.setStatus(ShipmentStatus.CREATED);

        return shipmentRepository.save(shipment);
    }
    
    public List<ShipmentTrackingDto> getCustomerShipments(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Shipment> shipments = shipmentRepository.findByCustomer(customer);

        return shipments.stream()
                .map(s -> new ShipmentTrackingDto(
                        s.getShipmentId(),
                        s.getStatus(),
                        s.getDriver() != null ? s.getDriver().getUsername() : null,
                        s.getEta(),
                        s.getOrigin(),
                        s.getDestination()
                ))
                .toList();
    }


    public Shipment assignDriver(Long shipmentId, Long driverId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if (driver.getRole() != Role.DRIVER) {
            throw new RuntimeException("User is not a driver");
        }

        shipment.setDriver(driver);
        shipment.setStatus(ShipmentStatus.ASSIGNED);

        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(Long shipmentId, ShipmentDto shipmentDto, Long customerId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));

        if (!shipment.getCustomer().getUserId().equals(customerId)) {
            throw new RuntimeException("Unauthorized to update this shipment");
        }

        if (shipment.getStatus() != ShipmentStatus.CREATED) {
            throw new RuntimeException("Shipment cannot be updated once it has been assigned");
        }

        shipment.setOrigin(shipmentDto.getOrigin());
        shipment.setDestination(shipmentDto.getDestination());

        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipmentStatus(Long shipmentId, ShipmentStatus status, Long updatedBy) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        User user = userRepository.findById(updatedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if the user is a driver
        if (user.getRole() != Role.DRIVER) {
            throw new RuntimeException("Only drivers can update shipment status");
        }

        // Check if this driver is assigned to this shipment
        if (shipment.getDriver() == null || !shipment.getDriver().getUserId().equals(updatedBy)) {
            throw new RuntimeException("You are not assigned to this shipment");
        }

        shipment.setStatus(status);

        ShipmentLog shipmentLog = new ShipmentLog();
        shipmentLog.setShipment(shipment);
        shipmentLog.setStatus(status);
        shipmentLog.setUpdatedBy(user);
        shipmentLogRepository.save(shipmentLog);

        return shipmentRepository.save(shipment);
    }


    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public List<Shipment> getShipmentsByCustomer(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return shipmentRepository.findByCustomer(customer);
    }

    public List<Shipment> getShipmentsByDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        return shipmentRepository.findByDriver(driver);
    }

    public Shipment updateShipmentStatusAndEta(Long shipmentId, ShipmentStatus status, LocalDate eta, Long driverId) throws AccessDeniedException {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));

        // Ensure driver is assigned
        if (!shipment.getDriver().getUserId().equals(driverId)) {
            throw new AccessDeniedException("You are not assigned to this shipment");
        }

        if (status != null) shipment.setStatus(status);
        if (eta != null) shipment.setEta(eta); // store only LocalDate

        return shipmentRepository.save(shipment);
    }



    public ShipmentTrackingDto getCustomerShipmentById(Long customerId, Long shipmentId) throws AccessDeniedException {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));

        // Ensure the shipment belongs to the customer
        if (!shipment.getCustomer().getUserId().equals(customerId)) {
            throw new AccessDeniedException("You don't have access to this shipment");
        }

        return new ShipmentTrackingDto(
                shipment.getShipmentId(),
                shipment.getStatus(),
                shipment.getDriver() != null ? shipment.getDriver().getUsername() : null,
                shipment.getEta(),
                shipment.getOrigin(),
                shipment.getDestination()
        );
    }

    

}
