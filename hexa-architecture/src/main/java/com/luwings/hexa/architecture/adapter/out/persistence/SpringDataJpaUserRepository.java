package com.luwings.hexa.architecture.adapter.out.persistence;

import com.luwings.hexa.architecture.adapter.UserEntity;
import com.luwings.hexa.architecture.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaUserRepository extends JpaRepository<UserEntity, Long> {
  UserEntity save(UserEntity user);
}