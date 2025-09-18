package org.cqrs.springcqrs.repository;

import org.cqrs.springcqrs.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}