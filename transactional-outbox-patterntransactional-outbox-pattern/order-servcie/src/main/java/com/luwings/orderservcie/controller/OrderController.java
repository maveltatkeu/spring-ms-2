package com.luwings.orderservcie.controller;

import com.luwings.orderservcie.common.dto.OrderRequestDTO;
import com.luwings.orderservcie.entity.Order;
import com.luwings.orderservcie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}