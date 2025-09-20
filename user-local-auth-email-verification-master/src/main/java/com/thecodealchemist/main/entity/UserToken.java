package com.thecodealchemist.main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "user_token")
@Getter
@Setter
public class UserToken {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String token;
}
