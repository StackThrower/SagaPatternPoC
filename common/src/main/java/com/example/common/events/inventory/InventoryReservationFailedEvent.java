package com.example.common.events.inventory;

import com.example.common.events.DomainEvent;

public class InventoryReservationFailedEvent extends DomainEvent {
    private final String productId;
    private final int requestedQuantity;
    private final int availableQuantity;
    private final String orderId;
    private final String reason;

    public InventoryReservationFailedEvent(String sagaId, String productId, int requestedQuantity,
                                         int availableQuantity, String orderId, String reason) {
        super(sagaId);
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getProductId() { return productId; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }
}
