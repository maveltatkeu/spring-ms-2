package com.partenairedecode.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //   Active la fonctionnalité de serveur Eureka
public class DiscoveryServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscoveryServerApplication.class, args);
  }

}
