package com.example.orderservice.controller;

import com.example.common.dto.OrderDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderService.createOrder(
            orderDto.getCustomerId(),
            orderDto.getProductId(),
            orderDto.getQuantity(),
            orderDto.getAmount()
        );

        OrderDto response = new OrderDto(
            order.getOrderId(),
            order.getCustomerId(),
            order.getProductId(),
            order.getQuantity(),
            order.getAmount(),
            order.getStatus().toString()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrder(orderId);

        OrderDto response = new OrderDto(
            order.getOrderId(),
            order.getCustomerId(),
            order.getProductId(),
            order.getQuantity(),
            order.getAmount(),
            order.getStatus().toString()
        );

        return ResponseEntity.ok(response);
    }
}
