	package com.logistics.shipment_tracker.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @PutMapping("/shipments/{id}/update")
    public ResponseEntity<Shipment> updateShipmentStatusAndEta(
            @PathVariable Long id,
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eta,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {

        Long driverId = userDetails.getId();

        return ResponseEntity.ok(
            shipmentService.updateShipmentStatusAndEta(id, status, eta, driverId)
        );
    }



}
