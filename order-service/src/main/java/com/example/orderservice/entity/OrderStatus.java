package com.example.orderservice.entity;

public enum OrderStatus {
    CREATED,
    PAYMENT_PROCESSING,
    INVENTORY_RESERVED,
    COMPLETED,
    CANCELLED,
    FAILED
}
