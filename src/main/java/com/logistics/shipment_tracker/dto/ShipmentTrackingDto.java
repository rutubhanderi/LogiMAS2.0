package com.logistics.shipment_tracker.dto;

import com.logistics.shipment_tracker.model.ShipmentStatus;

import java.time.LocalDate;


public class ShipmentTrackingDto {

    private Long shipmentId;
    private ShipmentStatus status;
    private String driverName;
    private LocalDate eta;
    private String origin;
    private String destination;

    // No-args constructor
    public ShipmentTrackingDto() {}

    // All-args constructor
    public ShipmentTrackingDto(Long shipmentId, ShipmentStatus status, String driverName,
                               LocalDate eta, String origin, String destination) {
        this.shipmentId = shipmentId;
        this.status = status;
        this.driverName = driverName;
        this.eta = eta;
        this.origin = origin;
        this.destination = destination;
    }

    

	// Getters and setters
    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public LocalDate getEta() { return eta; }
    public void setEta(LocalDate eta) { this.eta = eta; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
}
