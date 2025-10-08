package com.logistics.shipment_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String orderId;

    @NotBlank
    @Column(nullable = false)
    private String origin;

    @NotBlank
    @Column(nullable = false)
    private String destination;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    private LocalDateTime expectedArrival;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",nullable= true)
    private User assignedDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private User customer;

    // Constructors
    public Shipment() {}

    public Shipment(String orderId, String origin, String destination, ShipmentStatus status, LocalDateTime expectedArrival) {
        this.orderId = orderId;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.expectedArrival = expectedArrival;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrigin() { return origin; }

    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public ShipmentStatus getStatus() { return status; }

    public void setStatus(ShipmentStatus status) { this.status = status; }

    public LocalDateTime getExpectedArrival() { return expectedArrival; }

    public void setExpectedArrival(LocalDateTime expectedArrival) { this.expectedArrival = expectedArrival; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getAssignedDriver() { return assignedDriver; }

    public void setAssignedDriver(User assignedDriver) { this.assignedDriver = assignedDriver; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
}
