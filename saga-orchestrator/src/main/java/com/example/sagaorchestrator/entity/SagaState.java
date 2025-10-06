package com.example.sagaorchestrator.entity;

public enum SagaState {
    STARTED,
    PAYMENT_PROCESSING,
    PAYMENT_COMPLETED,
    INVENTORY_PROCESSING,
    INVENTORY_RESERVED,
    COMPLETED,
    FAILED,
    COMPENSATING,
    COMPENSATED
}
