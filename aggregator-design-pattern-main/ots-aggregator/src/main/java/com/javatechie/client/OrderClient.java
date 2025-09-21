package com.javatechie.client;

import com.javatechie.dto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderClient {

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(OrderClient.class);


    public Order fetchOrder(String orderId) {
        try {
            logger.info("fetching order details for orderId {}", orderId);
            Order order = restTemplate.getForObject("http://localhost:8081/orders/" + orderId, Order.class);
            logger.info("successfully fetched order details {}", order);
            return order;
        } catch (Exception ex) {
            logger.error("Exception occurred while fetching order details {}", ex.getMessage());
            return null;
        }
    }
}
