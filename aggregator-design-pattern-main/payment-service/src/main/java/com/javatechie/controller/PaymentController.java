package com.javatechie.controller;
import com.javatechie.dto.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping("/{orderId}")
    public Payment getPayment(@PathVariable String orderId) {
        return new Payment(orderId, "Paid", "Credit Card");
    }
}
