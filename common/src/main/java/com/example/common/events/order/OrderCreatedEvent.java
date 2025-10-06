package com.example.common.events.order;

import com.example.common.events.DomainEvent;

import java.math.BigDecimal;

public class OrderCreatedEvent extends DomainEvent {
    private final String orderId;
    private final String customerId;
    private final String productId;
    private final int quantity;
    private final BigDecimal amount;

    public OrderCreatedEvent(String sagaId, String orderId, String customerId,
                           String productId, int quantity, BigDecimal amount) {
        super(sagaId);
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
}

