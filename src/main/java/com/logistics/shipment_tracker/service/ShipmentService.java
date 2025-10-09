package com.logistics.shipment_tracker.service;

import com.logistics.shipment_tracker.dto.ShipmentDto;
import com.logistics.shipment_tracker.exception.ResourceNotFoundException;
import com.logistics.shipment_tracker.model.*;
import com.logistics.shipment_tracker.repository.ShipmentLogRepository;
import com.logistics.shipment_tracker.repository.ShipmentRepository;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
