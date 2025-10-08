package com.logistics.shipment_tracker.repository;

import com.logistics.shipment_tracker.model.LocationUpdate;
import com.logistics.shipment_tracker.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationUpdateRepository extends JpaRepository<LocationUpdate, Long> {
    List<LocationUpdate> findByShipmentOrderByTimestampAsc(Shipment shipment);
}
