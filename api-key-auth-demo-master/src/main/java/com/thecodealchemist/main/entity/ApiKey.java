package com.thecodealchemist.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "apiKeys")
@Setter
@Getter
public class ApiKey {
    @Id
    @GeneratedValue
    private Long id;
    private String apiKey;
    private String owner;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
