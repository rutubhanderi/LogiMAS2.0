package com.logistics.shipment_tracker.controller;

import com.logistics.shipment_tracker.model.Role;
import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.model.ShipmentStatus;
import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.repository.ShipmentRepository;
import com.logistics.shipment_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UserRepository userRepository;


    // ---------------- CREATE SHIPMENT ----------------
    @PostMapping("/shipment")
    public ResponseEntity<?> createShipment(@Valid @RequestBody Shipment shipment) {
        if (shipmentRepository.findByOrderId(shipment.getOrderId()).isPresent()) {
            return ResponseEntity.badRequest().body("Order ID already exists!");
        }

        shipment.setCustomer(null);
        shipment.setAssignedDriver(null);
        shipmentRepository.save(shipment);
        return ResponseEntity.ok("Shipment created successfully! Customer and driver can be assigned later.");
    }


    // ---------------- UPDATE SHIPMENT BY ID ----------------
    @PutMapping("/shipment/{id}")
    public ResponseEntity<?> updateShipmentById(@PathVariable Long id, @Valid @RequestBody Shipment updatedShipment) {
        Optional<Shipment> existingShipmentOpt = shipmentRepository.findById(id);
        if (existingShipmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found with ID: " + id);
        }

        Shipment shipment = existingShipmentOpt.get();
        shipment.setOrigin(updatedShipment.getOrigin());
        shipment.setDestination(updatedShipment.getDestination());
        shipment.setStatus(updatedShipment.getStatus());
        shipment.setExpectedArrival(updatedShipment.getExpectedArrival());
        shipmentRepository.save(shipment);

        return ResponseEntity.ok("Shipment updated successfully!");
    }


    // ---------------- UPDATE SHIPMENT BY ORDER ID ----------------
    @PutMapping("/shipment/{orderId}/update")
    public ResponseEntity<?> updateShipmentByOrderId(
            @PathVariable String orderId,
            @RequestBody Map<String, Object> updates
    ) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findByOrderId(orderId);
        if (shipmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found with Order ID: " + orderId);
        }

        Shipment shipment = shipmentOpt.get();

        if (updates.containsKey("origin")) {
            shipment.setOrigin((String) updates.get("origin"));
        }
        if (updates.containsKey("destination")) {
            shipment.setDestination((String) updates.get("destination"));
        }
        if (updates.containsKey("expectedArrival")) {
            try {
                shipment.setExpectedArrival(LocalDateTime.parse((String) updates.get("expectedArrival")));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid date format for expectedArrival! Use ISO format (e.g. 2025-10-10T15:30:00)");
            }
        }
        if (updates.containsKey("status")) {
            try {
                shipment.setStatus(ShipmentStatus.valueOf((String) updates.get("status")));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid status value! Use one of: CREATED, ASSIGNED, IN_TRANSIT, DELIVERED");
            }
        }

        shipmentRepository.save(shipment);
        return ResponseEntity.ok("Shipment updated successfully!");
    }


    // ---------------- ASSIGN DRIVER TO SHIPMENT ----------------
    @PostMapping("/shipment/{orderId}/assign-driver")
    public ResponseEntity<?> assignDriver(
            @PathVariable String orderId,
            @RequestParam(required = false) Long driverId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name
    ) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findByOrderId(orderId);
        if (shipmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found with Order ID: " + orderId);
        }

        Optional<User> driverOpt = Optional.empty();
        if (driverId != null) driverOpt = userRepository.findById(driverId);
        else if (email != null) driverOpt = userRepository.findByEmail(email);
        else if (name != null) driverOpt = userRepository.findByName(name);

        if (driverOpt.isEmpty() || driverOpt.get().getRole() != Role.DRIVER) {
            return ResponseEntity.badRequest().body("Driver not found or invalid role!");
        }

        Shipment shipment = shipmentOpt.get();
        shipment.setAssignedDriver(driverOpt.get());
        shipment.setStatus(ShipmentStatus.ASSIGNED);
        shipmentRepository.save(shipment);

        return ResponseEntity.ok("Driver assigned successfully!");
    }


    // ---------------- ASSIGN CUSTOMER TO SHIPMENT ----------------
    @PostMapping("/shipment/{orderId}/assign-customer")
    public ResponseEntity<?> assignCustomer(
            @PathVariable String orderId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name
    ) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findByOrderId(orderId);
        if (shipmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found with Order ID: " + orderId);
        }

        Optional<User> customerOpt = Optional.empty();
        if (customerId != null) customerOpt = userRepository.findById(customerId);
        else if (email != null) customerOpt = userRepository.findByEmail(email);
        else if (name != null) customerOpt = userRepository.findByName(name);

        if (customerOpt.isEmpty() || customerOpt.get().getRole() != Role.CUSTOMER) {
            return ResponseEntity.badRequest().body("Customer not found or invalid role!");
        }

        Shipment shipment = shipmentOpt.get();
        shipment.setCustomer(customerOpt.get());
        shipmentRepository.save(shipment);

        return ResponseEntity.ok("Customer assigned successfully!");
    }


    // ---------------- GET ALL SHIPMENTS ----------------
    @GetMapping("/shipments")
    public ResponseEntity<List<Shipment>> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return ResponseEntity.ok(shipments);
    }


    // ---------------- DELETE SHIPMENT ----------------
    @DeleteMapping("/shipment/{id}")
    public ResponseEntity<?> deleteShipment(@PathVariable Long id) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findById(id);
        if (shipmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found with ID: " + id);
        }

        shipmentRepository.deleteById(id);
        return ResponseEntity.ok("Shipment deleted successfully!");
    }
}
