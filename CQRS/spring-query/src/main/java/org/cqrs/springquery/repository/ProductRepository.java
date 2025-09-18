package org.cqrs.springquery.repository;

import org.cqrs.springquery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}