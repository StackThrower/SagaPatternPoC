package com.example.common.saga;

import java.util.UUID;

public abstract class SagaStep {
    private final String stepId;
    private final String stepName;

    public SagaStep(String stepName) {
        this.stepId = UUID.randomUUID().toString();
        this.stepName = stepName;
    }

    public abstract void execute();
    public abstract void compensate();

    public String getStepId() { return stepId; }
    public String getStepName() { return stepName; }
}
