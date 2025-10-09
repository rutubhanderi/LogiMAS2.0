package com.logistics.shipment_tracker.dto;

import com.logistics.shipment_tracker.model.ShipmentStatus;
import java.time.LocalDateTime;

public class ShipmentDto {

    private Long shipmentId;
    private Long customerId;
    private Long driverId;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private LocalDateTime eta;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	public Long getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getDriverId() {
		return driverId;
	}
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public ShipmentStatus getStatus() {
		return status;
	}
	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}
	public LocalDateTime getEta() {
		return eta;
	}
	public void setEta(LocalDateTime eta) {
		this.eta = eta;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
