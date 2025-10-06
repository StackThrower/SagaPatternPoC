package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int availableQuantity;

    @Column(nullable = false)
    private int reservedQuantity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Inventory() {
        this.createdAt = LocalDateTime.now();
        this.reservedQuantity = 0;
    }

    public Inventory(String productId, String productName, int availableQuantity) {
        this();
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
