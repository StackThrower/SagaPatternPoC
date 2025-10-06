package com.example.common.events.inventory;

import com.example.common.events.DomainEvent;

public class InventoryReservedEvent extends DomainEvent {
    private final String reservationId;
    private final String productId;
    private final int quantity;
    private final String orderId;

    public InventoryReservedEvent(String sagaId, String reservationId, String productId,
                                int quantity, String orderId) {
        super(sagaId);
        this.reservationId = reservationId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public String getReservationId() { return reservationId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public String getOrderId() { return orderId; }
}
