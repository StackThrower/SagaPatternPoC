package com.example.common.saga;

public enum SagaStatus {
    STARTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    COMPENSATING,
    COMPENSATED
}
