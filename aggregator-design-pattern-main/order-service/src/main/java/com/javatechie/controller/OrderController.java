package com.javatechie.controller;

import com.javatechie.dto.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return new Order(orderId, "Laptop, Mouse", "Shipped", LocalDate.now());
    }
}
