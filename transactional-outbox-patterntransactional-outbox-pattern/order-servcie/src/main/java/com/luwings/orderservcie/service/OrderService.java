package com.luwings.orderservcie.service;

import com.luwings.orderservcie.common.dto.OrderRequestDTO;
import com.luwings.orderservcie.common.mapper.OrderDTOtoEntityMapper;
import com.luwings.orderservcie.common.mapper.OrderEntityToOutboxEntityMapper;
import com.luwings.orderservcie.entity.Order;
import com.luwings.orderservcie.entity.Outbox;
import com.luwings.orderservcie.repository.OrderRepository;
import com.luwings.orderservcie.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

  @Autowired
  private OrderDTOtoEntityMapper orderDTOtoEntityMapper;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OutboxRepository outboxRepository;

  @Autowired
  private OrderEntityToOutboxEntityMapper orderEntityToOutboxEntityMapper;


  @Transactional
  public Order createOrder(OrderRequestDTO orderRequestDTO) {

    Order order = orderDTOtoEntityMapper.map(orderRequestDTO);
    order = orderRepository.save(order);

    Outbox outbox = orderEntityToOutboxEntityMapper.map(order);
    outboxRepository.save(outbox);

    return order;
  }
}