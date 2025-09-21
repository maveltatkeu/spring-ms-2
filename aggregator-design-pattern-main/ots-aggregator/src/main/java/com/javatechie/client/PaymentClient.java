package com.javatechie.client;

import com.javatechie.dto.Order;
import com.javatechie.dto.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentClient {

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(PaymentClient.class);


    public Payment fetchPayment(String orderId) {
        try {
            logger.info("fetching payment details for orderId {}", orderId);
            Payment payment = restTemplate.getForObject("http://localhost:8083/payments/" + orderId, Payment.class);
            logger.info("successfully fetched payment details {}", payment);
            return payment;
        } catch (Exception ex) {
            logger.error("Exception occurred while fetching payment details {}", ex.getMessage());
            return null;
        }
    }
}
