package com.luwings.orderservcie.repository;

import com.luwings.orderservcie.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}