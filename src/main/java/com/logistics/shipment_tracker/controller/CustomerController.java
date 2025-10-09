package com.logistics.shipment_tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.shipment_tracker.config.security.CustomUserDetails;
import com.logistics.shipment_tracker.dto.ShipmentDto;
import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.service.ShipmentService;

@RestController
@RequestMapping("/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping("/shipments")
    public ResponseEntity<Shipment> createShipment(@RequestBody ShipmentDto shipmentDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getId();
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDto, customerId));
    }

    @GetMapping("/shipments")
    public ResponseEntity<List<Shipment>> getCustomerShipments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getId();
        return ResponseEntity.ok(shipmentService.getShipmentsByCustomer(customerId));
    }

    @PutMapping("/shipments/{id}")
    public ResponseEntity<Shipment> updateShipment(@PathVariable Long id, @RequestBody ShipmentDto shipmentDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getId();
        return ResponseEntity.ok(shipmentService.updateShipment(id, shipmentDto, customerId));
    }
}
