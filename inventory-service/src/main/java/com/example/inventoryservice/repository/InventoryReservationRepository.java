package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, String> {
    InventoryReservation findByOrderId(String orderId);
}
