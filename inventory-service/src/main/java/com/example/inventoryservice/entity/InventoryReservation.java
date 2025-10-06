package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_reservations")
public class InventoryReservation {
    @Id
    private String reservationId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public InventoryReservation() {
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
    }

    public InventoryReservation(String reservationId, String productId, String orderId, int quantity) {
        this();
        this.reservationId = reservationId;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
