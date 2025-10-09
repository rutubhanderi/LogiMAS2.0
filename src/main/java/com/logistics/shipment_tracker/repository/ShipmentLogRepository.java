package com.logistics.shipment_tracker.repository;

import com.logistics.shipment_tracker.model.Shipment;
import com.logistics.shipment_tracker.model.ShipmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentLogRepository extends JpaRepository<ShipmentLog, Long> {

    List<ShipmentLog> findByShipment(Shipment shipment);
}
