package com.thecodealchemist.main.repository;

import com.thecodealchemist.main.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByOwnerAndIsActiveTrue(String owner);

    Optional<ApiKey> findByOwnerAndIsActiveTrueAndExpiresAtAfter(String owner, LocalDateTime after);
}
