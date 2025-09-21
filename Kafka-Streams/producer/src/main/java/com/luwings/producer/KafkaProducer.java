package com.luwings.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 20 / Sep / 2025
 * Time: 13 : 16
 */
@RestController
@RequestMapping("/api/v1")
public class KafkaProducer {

  private final KafkaTemplate<String, RiderLocation> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, RiderLocation> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }


}
