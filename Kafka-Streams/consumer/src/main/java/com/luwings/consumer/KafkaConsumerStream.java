package com.luwings.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 20 / Sep / 2025
 * Time: 14 : 12
 */

@Slf4j
@Configuration
public class KafkaConsumerStream {

  @Bean
  public Consumer<RiderLocation> processRiderLocaltion() {
    return location -> {
      log.info("Received: {} @ {},{}", location.getRiderId(), location.getLatitude(), location.getLongitude());
    };
  }
  @Bean
  public Consumer<String> processRiderStatus() {
    return location -> {
      log.info("Received Status: {}", location);
    };
  }
}
