package com.logistics.shipment_tracker.repository;

import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrderId(String orderId);
    
    List<Shipment> findByAssignedDriver(User driver);
    
    List<Shipment> findByStatus(String status);
}