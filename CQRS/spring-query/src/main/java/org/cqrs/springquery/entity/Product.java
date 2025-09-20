package org.cqrs.springquery.entity;

import lombok.Data;

@Data
public class Product {
  private Long id;
  private String name;
  private String description;
  private double price;
}