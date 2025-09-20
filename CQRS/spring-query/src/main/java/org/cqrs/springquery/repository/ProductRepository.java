package org.cqrs.springquery.repository;

import org.cqrs.springquery.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  ProductEntity findByExternalId(long id);
}