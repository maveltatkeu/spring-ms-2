package org.cqrs.springcqrs.service;

import lombok.extern.slf4j.Slf4j;
import org.cqrs.springcqrs.dto.ProductDto;
import org.cqrs.springcqrs.dto.ProductEvent;
import org.cqrs.springcqrs.entity.Product;
import org.cqrs.springcqrs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ProductCommandService {

  @Value("${application.kafka.topic-name}")
  private String topicName;

  @Autowired
  private ProductRepository repository;

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  private static Product getProductFromDto(ProductDto productDto) {
    return Product.builder()
        .description(productDto.description())
        .name(productDto.name())
        .price(productDto.price())
        .build();
  }

  public Product createProduct(ProductDto productDto) {
    Product product = getProductFromDto(productDto);

    Product productDO = repository.save(product);
    ProductEvent event = new ProductEvent("CREATE_PRODUCT", productDO);
    sendForSynchronize(event);

    return productDO;
  }

  private void sendForSynchronize(ProductEvent event) {
    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, event);

    future.whenComplete((r, exception) -> {
      if (exception == null) {
        log.info("Sent message: " + event + " with offset : [" + r.getRecordMetadata().offset() + "]");
      } else {
        log.error("Unable to send message: " + event + " due to : " + exception.getMessage());
      }
    });
  }

  public Product updateProduct(long id, ProductDto productDto) {
    Product existingProduct = repository.findById(id).get();
    Product newProduct = getProductFromDto(productDto);
    existingProduct.setName(newProduct.getName());
    existingProduct.setPrice(newProduct.getPrice());
    existingProduct.setDescription(newProduct.getDescription());
    Product productDO = repository.save(existingProduct);
    ProductEvent event = new ProductEvent("UPDATE_PRODUCT", productDO);
    sendForSynchronize(event);
    return productDO;
  }

}