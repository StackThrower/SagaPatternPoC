package com.example.sagaorchestrator.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saga_instances")
public class SagaInstance {
    @Id
    private String sagaId;

    @Column(nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private SagaState state;

    @Column(nullable = false)
    private String currentStep;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String failureReason;

    public SagaInstance() {
        this.createdAt = LocalDateTime.now();
        this.state = SagaState.STARTED;
        this.currentStep = "ORDER_CREATED";
    }

    public SagaInstance(String sagaId, String orderId) {
        this();
        this.sagaId = sagaId;
        this.orderId = orderId;
    }

    // Getters and Setters
    public String getSagaId() { return sagaId; }
    public void setSagaId(String sagaId) { this.sagaId = sagaId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public SagaState getState() { return state; }
    public void setState(SagaState state) {
        this.state = state;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCurrentStep() { return currentStep; }
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
