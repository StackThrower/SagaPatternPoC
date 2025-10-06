package com.example.common.events.payment;

import com.example.common.events.DomainEvent;
import java.math.BigDecimal;

public class PaymentFailedEvent extends DomainEvent {
    private final String paymentId;
    private final String orderId;
    private final BigDecimal amount;
    private final String reason;

    public PaymentFailedEvent(String sagaId, String paymentId, String orderId,
                            BigDecimal amount, String reason) {
        super(sagaId);
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.reason = reason;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getReason() { return reason; }
}
