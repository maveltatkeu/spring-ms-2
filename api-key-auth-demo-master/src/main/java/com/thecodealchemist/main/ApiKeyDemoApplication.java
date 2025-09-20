package com.thecodealchemist.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ApiKeyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiKeyDemoApplication.class, args);
	}

}
