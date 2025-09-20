package org.cqrs.springquery.service;

import lombok.extern.slf4j.Slf4j;
import org.cqrs.springquery.dto.ProductEvent;
import org.cqrs.springquery.entity.Product;
import org.cqrs.springquery.entity.ProductEntity;
import org.cqrs.springquery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductQueryService {

  @Value("${application.kafka.topic-name}")
  private String topicName;

  @Autowired
  private ProductRepository repository;

  public List<ProductEntity> getProducts() {
    return repository.findAll();
  }

  @KafkaListener(topics = "${application.kafka.topic-name}",
      groupId = "${application.kafka.groupId",
      containerFactory = "customerKafkaListenerContainerFactory"
  )
  public void processProductEvents(ProductEvent productEvent) {
    Product product = productEvent.getProduct();
    ProductEntity productDO =  ProductEntity.builder()
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .externalId(product.getId())
        .build();

    if (productEvent.getEventType().equals("CREATE_PRODUCT")) {
      repository.save(productDO);
    }
    if (productEvent.getEventType().equals("UPDATE_PRODUCT")) {
      ProductEntity existingProduct = repository.findByExternalId(product.getId());
      existingProduct.setPrice(product.getPrice());
      existingProduct.setDescription(product.getDescription());
      repository.save(existingProduct);
    }
    log.info("Processed: [ {} ]", productEvent);
  }
}