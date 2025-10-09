package com.logistics.shipment_tracker.repository;

import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.model.ShipmentStatus;
import com.logistics.shipment_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByCustomer(User customer);

    List<Shipment> findByDriver(User driver);

    List<Shipment> findByStatus(ShipmentStatus status);
}