package org.cqrs.springcqrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cqrs.springcqrs.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
  private String eventType;
  private Product product;
}