package com.logistics.shipment_tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.shipment_tracker.config.security.CustomUserDetails;
import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.model.ShipmentStatus;
import com.logistics.shipment_tracker.service.ShipmentService;

@RestController
@RequestMapping("/driver")
@PreAuthorize("hasRole('DRIVER')")
public class DriverController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping("/shipments")
    public ResponseEntity<List<Shipment>> getAssignedShipments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long driverId = userDetails.getId();
        return ResponseEntity.ok(shipmentService.getShipmentsByDriver(driverId));
    }

    @PutMapping("/shipments/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(@PathVariable Long id, @RequestParam ShipmentStatus status, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long driverId = userDetails.getId();
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, status, driverId));
    }
}
