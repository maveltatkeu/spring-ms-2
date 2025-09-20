package com.thecodealchemist.main.db;

import com.thecodealchemist.main.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken, Long> {
    UserToken findByToken(String token);
}
