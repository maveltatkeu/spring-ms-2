package com.javatechie.client;

import com.javatechie.dto.Payment;
import com.javatechie.dto.Shipping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShippingClient {


    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(ShippingClient.class);


    public Shipping fetchShippingDetails(String orderId) {
        try {
            logger.info("fetching Shipping details for orderId {}", orderId);
            Shipping shipping = restTemplate.getForObject("http://localhost:8082/shipping/" + orderId, Shipping.class);
            logger.info("successfully fetched shipping details {}", shipping);
            return shipping;
        } catch (Exception ex) {
            logger.error("Exception occurred while fetching shipping details {}", ex.getMessage());
            return null;
        }
    }
}
