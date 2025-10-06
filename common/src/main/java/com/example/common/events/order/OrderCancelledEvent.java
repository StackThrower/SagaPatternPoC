package com.example.common.events.order;

import com.example.common.events.DomainEvent;

public class OrderCancelledEvent extends DomainEvent {
    private final String orderId;
    private final String reason;

    public OrderCancelledEvent(String sagaId, String orderId, String reason) {
        super(sagaId);
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }
}
