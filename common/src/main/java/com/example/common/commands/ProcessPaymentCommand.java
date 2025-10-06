package com.example.common.commands;

import java.math.BigDecimal;

public class ProcessPaymentCommand {
    private final String paymentId;
    private final String orderId;
    private final String customerId;
    private final BigDecimal amount;
    private final String sagaId;

    public ProcessPaymentCommand(String paymentId, String orderId, String customerId,
                               BigDecimal amount, String sagaId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.sagaId = sagaId;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public String getSagaId() { return sagaId; }
}
