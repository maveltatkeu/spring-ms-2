package com.javatechie.controller;

import com.javatechie.client.OrderClient;
import com.javatechie.client.PaymentClient;
import com.javatechie.client.ShippingClient;
import com.javatechie.dto.Order;
import com.javatechie.dto.OrderDetails;
import com.javatechie.dto.Payment;
import com.javatechie.dto.Shipping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-aggregator")
public class OTSAggregatorController {

    private final OrderClient orderClient;

    private final PaymentClient paymentClient;

    private final ShippingClient shippingClient;

    public OTSAggregatorController(OrderClient orderClient,
                                   PaymentClient paymentClient,
                                   ShippingClient shippingClient) {
        this.orderClient = orderClient;
        this.paymentClient = paymentClient;
        this.shippingClient = shippingClient;
    }

    @GetMapping("/{orderId}")
    public OrderDetails getOrderDetails(@PathVariable String orderId) {

        Order order = orderClient.fetchOrder(orderId);
        Shipping shipping = shippingClient.fetchShippingDetails(orderId);
        Payment payment=paymentClient.fetchPayment(orderId);

        return  new OrderDetails(order, payment, shipping);
    }
}
