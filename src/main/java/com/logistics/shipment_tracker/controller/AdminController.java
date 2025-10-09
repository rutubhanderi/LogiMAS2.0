package com.logistics.shipment_tracker.controller;

import com.logistics.shipment_tracker.dto.ShipmentDto;
import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.model.User;
import com.logistics.shipment_tracker.service.ShipmentService;
import com.logistics.shipment_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShipmentService shipmentService;

    // User Management
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody com.logistics.shipment_tracker.dto.UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    // Shipment Management
    @GetMapping("/shipments")
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @PostMapping("/shipments")
    public ResponseEntity<Shipment> createShipment(@RequestBody ShipmentDto shipmentDto) {
        // Assuming customerId is part of the DTO or handled differently
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDto, shipmentDto.getCustomerId()));
    }

    @PutMapping("/shipments/{id}/assign-driver/{driverId}")
    public ResponseEntity<Shipment> assignDriverToShipment(@PathVariable Long id, @PathVariable Long driverId) {
        return ResponseEntity.ok(shipmentService.assignDriver(id, driverId));
    }
}

