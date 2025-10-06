package com.example.common.events.payment;

import com.example.common.events.DomainEvent;
import java.math.BigDecimal;

public class PaymentProcessedEvent extends DomainEvent {
    private final String paymentId;
    private final String orderId;
    private final BigDecimal amount;
    private final String customerId;

    public PaymentProcessedEvent(String sagaId, String paymentId, String orderId,
                               BigDecimal amount, String customerId) {
        super(sagaId);
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.customerId = customerId;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getCustomerId() { return customerId; }
}
