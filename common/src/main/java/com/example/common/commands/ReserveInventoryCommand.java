package com.example.common.commands;

public class ReserveInventoryCommand {
    private final String reservationId;
    private final String productId;
    private final int quantity;
    private final String orderId;
    private final String sagaId;

    public ReserveInventoryCommand(String reservationId, String productId, int quantity,
                                 String orderId, String sagaId) {
        this.reservationId = reservationId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderId = orderId;
        this.sagaId = sagaId;
    }

    public String getReservationId() { return reservationId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public String getOrderId() { return orderId; }
    public String getSagaId() { return sagaId; }
}
