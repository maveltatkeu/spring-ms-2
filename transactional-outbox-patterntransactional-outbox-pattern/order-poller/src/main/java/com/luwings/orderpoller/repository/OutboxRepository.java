package com.luwings.orderpoller.repository;

import com.luwings.orderpoller.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox,Long> {

    //un-processed records
    List<Outbox>  findByProcessedFalse();
}