package com.luwings.springsecurityhttpbasicauth.repository;

import com.luwings.springsecurityhttpbasicauth.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}