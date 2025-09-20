package com.luwings.springsecurityhttpbasicauth.repository;

import com.luwings.springsecurityhttpbasicauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
}