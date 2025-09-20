package com.thecodealchemist.main.db;

import com.thecodealchemist.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
