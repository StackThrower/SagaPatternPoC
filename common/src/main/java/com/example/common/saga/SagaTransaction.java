package com.example.common.saga;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class SagaTransaction {
    private final String sagaId;
    private final List<SagaStep> steps;
    private final List<SagaStep> executedSteps;
    private SagaStatus status;

    public SagaTransaction() {
        this.sagaId = UUID.randomUUID().toString();
        this.steps = new ArrayList<>();
        this.executedSteps = new ArrayList<>();
        this.status = SagaStatus.STARTED;
    }

    public void addStep(SagaStep step) {
        steps.add(step);
    }

    public void execute() {
        try {
            for (SagaStep step : steps) {
                step.execute();
                executedSteps.add(step);
            }
            status = SagaStatus.COMPLETED;
        } catch (Exception e) {
            status = SagaStatus.FAILED;
            compensate();
        }
    }

    private void compensate() {
        // Execute compensation in reverse order
        for (int i = executedSteps.size() - 1; i >= 0; i--) {
            try {
                executedSteps.get(i).compensate();
            } catch (Exception e) {
                // Log compensation failure
                System.err.println("Compensation failed for step: " + executedSteps.get(i).getStepName());
            }
        }
        status = SagaStatus.COMPENSATED;
    }

    public String getSagaId() { return sagaId; }
    public SagaStatus getStatus() { return status; }
    public void setStatus(SagaStatus status) { this.status = status; }
}
