package com.example.sagaorchestrator.service;

import com.example.common.events.order.OrderCreatedEvent;
import com.example.common.events.payment.PaymentProcessedEvent;
import com.example.common.events.payment.PaymentFailedEvent;
import com.example.common.events.inventory.InventoryReservedEvent;
import com.example.common.events.inventory.InventoryReservationFailedEvent;
import com.example.sagaorchestrator.entity.SagaInstance;
import com.example.sagaorchestrator.entity.SagaState;
import com.example.sagaorchestrator.repository.SagaInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SagaOrchestrationService {

    @Autowired
    private SagaInstanceRepository sagaRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-events", groupId = "saga-orchestrator-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        // Create new saga instance
        SagaInstance saga = new SagaInstance(event.getSagaId(), event.getOrderId());
        saga.setState(SagaState.PAYMENT_PROCESSING);
        saga.setCurrentStep("PAYMENT_PROCESSING");
        sagaRepository.save(saga);

        System.out.println("Saga started for order: " + event.getOrderId());
    }

    @KafkaListener(topics = "payment-events", groupId = "saga-orchestrator-group")
    public void handlePaymentEvents(Object event) {
        if (event instanceof PaymentProcessedEvent paymentEvent) {
            handlePaymentProcessedEvent(paymentEvent);
        } else if (event instanceof PaymentFailedEvent paymentFailedEvent) {
            handlePaymentFailedEvent(paymentFailedEvent);
        }
    }

    private void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        SagaInstance saga = sagaRepository.findById(event.getSagaId()).orElse(null);
        if (saga != null) {
            saga.setState(SagaState.INVENTORY_PROCESSING);
            saga.setCurrentStep("INVENTORY_PROCESSING");
            sagaRepository.save(saga);

            System.out.println("Payment completed for saga: " + event.getSagaId() +
                             ", proceeding to inventory reservation");
        }
    }

    private void handlePaymentFailedEvent(PaymentFailedEvent event) {
        SagaInstance saga = sagaRepository.findById(event.getSagaId()).orElse(null);
        if (saga != null) {
            saga.setState(SagaState.FAILED);
            saga.setCurrentStep("PAYMENT_FAILED");
            saga.setFailureReason(event.getReason());
            sagaRepository.save(saga);

            // Start compensation
            compensateOrder(saga);

            System.out.println("Payment failed for saga: " + event.getSagaId() +
                             ", reason: " + event.getReason());
        }
    }

    @KafkaListener(topics = "inventory-events", groupId = "saga-orchestrator-group")
    public void handleInventoryEvents(Object event) {
        if (event instanceof InventoryReservedEvent inventoryEvent) {
            handleInventoryReservedEvent(inventoryEvent);
        } else if (event instanceof InventoryReservationFailedEvent inventoryFailedEvent) {
            handleInventoryReservationFailedEvent(inventoryFailedEvent);
        }
    }

    private void handleInventoryReservedEvent(InventoryReservedEvent event) {
        SagaInstance saga = sagaRepository.findById(event.getSagaId()).orElse(null);
        if (saga != null) {
            saga.setState(SagaState.COMPLETED);
            saga.setCurrentStep("COMPLETED");
            sagaRepository.save(saga);

            // Publish order completion event
            kafkaTemplate.send("notification-events",
                "Order " + saga.getOrderId() + " completed successfully");

            System.out.println("Saga completed successfully for order: " + saga.getOrderId());
        }
    }

    private void handleInventoryReservationFailedEvent(InventoryReservationFailedEvent event) {
        SagaInstance saga = sagaRepository.findById(event.getSagaId()).orElse(null);
        if (saga != null) {
            saga.setState(SagaState.FAILED);
            saga.setCurrentStep("INVENTORY_FAILED");
            saga.setFailureReason(event.getReason());
            sagaRepository.save(saga);

            // Start compensation
            compensatePaymentAndOrder(saga);

            System.out.println("Inventory reservation failed for saga: " + event.getSagaId() +
                             ", reason: " + event.getReason());
        }
    }

    private void compensateOrder(SagaInstance saga) {
        saga.setState(SagaState.COMPENSATING);
        sagaRepository.save(saga);

        // Send order cancellation command
        kafkaTemplate.send("compensation-events",
            "CANCEL_ORDER:" + saga.getOrderId() + ":" + saga.getSagaId());

        saga.setState(SagaState.COMPENSATED);
        sagaRepository.save(saga);
    }

    private void compensatePaymentAndOrder(SagaInstance saga) {
        saga.setState(SagaState.COMPENSATING);
        sagaRepository.save(saga);

        // Send payment refund command
        kafkaTemplate.send("compensation-events",
            "REFUND_PAYMENT:" + saga.getOrderId() + ":" + saga.getSagaId());

        // Send order cancellation command
        kafkaTemplate.send("compensation-events",
            "CANCEL_ORDER:" + saga.getOrderId() + ":" + saga.getSagaId());

        saga.setState(SagaState.COMPENSATED);
        sagaRepository.save(saga);
    }
}
