package com.luwings.springsecurityhttpbasicauth.repository;

import com.luwings.springsecurityhttpbasicauth.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    Optional<AuthorityEntity> findByName(String name);
}