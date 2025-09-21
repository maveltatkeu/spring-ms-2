package com.luwings.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 20 / Sep / 2025
 * Time: 13 : 16
 */
@Slf4j
@Configuration
public class KafkaProducerStream {
  private static final String[] ADJECTIVES = {
      "Whispering", "Ancient", "Silent", "Hidden", "Mystic", "Crimson", "Glimmering", "Lost"
  };
  private static final String[] NOUNS = {
      "Forest", "Mountain", "River", "Valley", "Hollow", "Lake", "Peak", "Stone"
  };
  private static final String[] PLACES = {
      "Sanctuary", "Realm", "Haven", "Grotto", "Domain", "Fortress", "Keep", "Citadel"
  };

  private static final Random RANDOM = new Random();


  @Bean
  public Supplier<RiderLocation> sendRiderLocation() {
    return () -> {
      var location = getRandomLocation();
      log.info("Sending: {}", location);
      return location;
    };
  }

  @Bean
  public Supplier<Message<String>> sendRiderStatus() {
    return () -> {
      Random random = new Random();
      var location = getRandomLocation();
      String status = random.nextBoolean() ? "Active" : "Inactive";
      log.info("Sending: Status - {}", status);
      return MessageBuilder
          .withPayload(location.getRiderId() + " - " + status)
          .setHeader(KafkaHeaders.KEY, location.getRiderId().getBytes())
          .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.TEXT_PLAIN_VALUE)
          .build();
    };
  }

  private RiderLocation getRandomLocation() {
    String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
    String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
    String place = PLACES[RANDOM.nextInt(PLACES.length)];

    RiderLocation rider = new RiderLocation(adjective + " " + noun + " " + place,
        RANDOM.nextDouble() * 180.0 - 90.0, RANDOM.nextDouble() * 360.0 - 180.0);
    return rider;
  }
}
