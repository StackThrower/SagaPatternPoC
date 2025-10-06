package com.example.notificationservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "notification-events", groupId = "notification-service-group")
    public void handleNotificationEvents(String message) {
        System.out.println("📧 NOTIFICATION: " + message);
        // In a real implementation, this would send emails, SMS, push notifications, etc.
    }

    @KafkaListener(topics = "compensation-events", groupId = "notification-service-group")
    public void handleCompensationEvents(String message) {
        System.out.println("⚠️  COMPENSATION: " + message);
        // Notify about compensation actions
    }
}
