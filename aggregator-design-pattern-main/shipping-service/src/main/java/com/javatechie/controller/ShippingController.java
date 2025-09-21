package com.javatechie.controller;
import com.javatechie.dto.Shipping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @GetMapping("/{orderId}")
    public Shipping getShipping(@PathVariable String orderId) {
        return new Shipping(orderId, "In Transit", "New York", "3 Days");
    }
}
