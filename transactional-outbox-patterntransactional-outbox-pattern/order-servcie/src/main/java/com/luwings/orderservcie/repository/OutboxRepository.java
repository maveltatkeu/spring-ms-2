package com.luwings.orderservcie.repository;

import com.luwings.orderservcie.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox,Long> {
}