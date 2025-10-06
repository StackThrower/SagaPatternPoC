package com.example.sagaorchestrator.repository;

import com.example.sagaorchestrator.entity.SagaInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaInstanceRepository extends JpaRepository<SagaInstance, String> {
    SagaInstance findByOrderId(String orderId);
}
