package com.example.orderservice.service;

import com.example.common.events.order.OrderCreatedEvent;
import com.example.common.events.order.OrderCancelledEvent;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Order createOrder(String customerId, String productId, int quantity, BigDecimal amount) {
        String orderId = UUID.randomUUID().toString();
        String sagaId = UUID.randomUUID().toString();

        Order order = new Order(orderId, customerId, productId, quantity, amount);
        order = orderRepository.save(order);

        // Publish OrderCreatedEvent to start the saga
        OrderCreatedEvent event = new OrderCreatedEvent(sagaId, orderId, customerId, productId, quantity, amount);
        kafkaTemplate.send("order-events", event);

        return order;
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(status);
        orderRepository.save(order);
    }

    public void cancelOrder(String orderId, String sagaId, String reason) {
        updateOrderStatus(orderId, OrderStatus.CANCELLED);

        // Publish OrderCancelledEvent
        OrderCancelledEvent event = new OrderCancelledEvent(sagaId, orderId, reason);
        kafkaTemplate.send("order-events", event);
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }
}
