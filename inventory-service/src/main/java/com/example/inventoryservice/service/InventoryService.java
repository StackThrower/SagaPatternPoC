package com.example.inventoryservice.service;

import com.example.common.events.payment.PaymentProcessedEvent;
import com.example.common.events.inventory.InventoryReservedEvent;
import com.example.common.events.inventory.InventoryReservationFailedEvent;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.entity.InventoryReservation;
import com.example.inventoryservice.entity.ReservationStatus;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.InventoryReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryReservationRepository reservationRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payment-events", groupId = "inventory-service-group")
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        // Only process if it's a PaymentProcessedEvent
        if (event instanceof PaymentProcessedEvent) {
            reserveInventory(event.getSagaId(), event.getOrderId(),
                           "PRODUCT-1", 1); // Simplified for demo
        }
    }

    @Transactional
    public void reserveInventory(String sagaId, String orderId, String productId, int quantity) {
        String reservationId = UUID.randomUUID().toString();

        Inventory inventory = inventoryRepository.findById(productId).orElse(null);

        if (inventory == null) {
            // Product not found
            InventoryReservationFailedEvent failureEvent = new InventoryReservationFailedEvent(
                sagaId, productId, quantity, 0, orderId, "Product not found");
            kafkaTemplate.send("inventory-events", failureEvent);
            return;
        }

        if (inventory.getAvailableQuantity() < quantity) {
            // Insufficient inventory
            InventoryReservationFailedEvent failureEvent = new InventoryReservationFailedEvent(
                sagaId, productId, quantity, inventory.getAvailableQuantity(),
                orderId, "Insufficient inventory");
            kafkaTemplate.send("inventory-events", failureEvent);
            return;
        }

        // Reserve inventory
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);

        // Create reservation record
        InventoryReservation reservation = new InventoryReservation(
            reservationId, productId, orderId, quantity);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservationRepository.save(reservation);

        // Publish success event
        InventoryReservedEvent successEvent = new InventoryReservedEvent(
            sagaId, reservationId, productId, quantity, orderId);
        kafkaTemplate.send("inventory-events", successEvent);
    }

    @Transactional
    public void releaseReservation(String orderId) {
        InventoryReservation reservation = reservationRepository.findByOrderId(orderId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.RESERVED) {
            Inventory inventory = inventoryRepository.findById(reservation.getProductId()).orElse(null);
            if (inventory != null) {
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() + reservation.getQuantity());
                inventory.setReservedQuantity(inventory.getReservedQuantity() - reservation.getQuantity());
                inventoryRepository.save(inventory);

                reservation.setStatus(ReservationStatus.CANCELLED);
                reservationRepository.save(reservation);
            }
        }
    }

    public void initializeInventory() {
        // Initialize some sample products
        if (inventoryRepository.count() == 0) {
            inventoryRepository.save(new Inventory("PRODUCT-1", "Sample Product", 100));
            inventoryRepository.save(new Inventory("PRODUCT-2", "Another Product", 50));
        }
    }
}
