package com.luwings.orderpoller.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OutBoxMessageConsumer {

  @KafkaListener(topics = "${order-poller-topic-name}",
      groupId = "${order-poller-group")
  public void consume(String payload) {
    log.info("Event consumed {} ", payload);
  }
}