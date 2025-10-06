package com.example.paymentservice.service;

import com.example.common.events.order.OrderCreatedEvent;
import com.example.common.events.payment.PaymentProcessedEvent;
import com.example.common.events.payment.PaymentFailedEvent;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private Random random = new Random();

    @KafkaListener(topics = "order-events", groupId = "payment-service-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        processPayment(event.getSagaId(), event.getOrderId(),
                      event.getCustomerId(), event.getAmount());
    }

    public void processPayment(String sagaId, String orderId, String customerId, BigDecimal amount) {
        String paymentId = "PAY-" + System.currentTimeMillis();

        Payment payment = new Payment(paymentId, orderId, customerId, amount);
        payment.setStatus(PaymentStatus.PROCESSING);
        payment = paymentRepository.save(payment);

        // Simulate payment processing (90% success rate)
        try {
            Thread.sleep(1000); // Simulate processing time

            if (random.nextDouble() < 0.9) { // 90% success rate
                payment.setStatus(PaymentStatus.COMPLETED);
                paymentRepository.save(payment);

                // Publish success event
                PaymentProcessedEvent successEvent = new PaymentProcessedEvent(
                    sagaId, paymentId, orderId, amount, customerId);
                kafkaTemplate.send("payment-events", successEvent);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);

                // Publish failure event
                PaymentFailedEvent failureEvent = new PaymentFailedEvent(
                    sagaId, paymentId, orderId, amount, "Insufficient funds");
                kafkaTemplate.send("payment-events", failureEvent);
            }
        } catch (InterruptedException e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            PaymentFailedEvent failureEvent = new PaymentFailedEvent(
                sagaId, paymentId, orderId, amount, "Processing error");
            kafkaTemplate.send("payment-events", failureEvent);
        }
    }

    public void refundPayment(String paymentId, String sagaId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null && payment.getStatus() == PaymentStatus.COMPLETED) {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }
    }
}
