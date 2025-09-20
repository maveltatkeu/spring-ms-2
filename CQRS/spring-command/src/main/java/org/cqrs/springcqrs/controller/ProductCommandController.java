package org.cqrs.springcqrs.controller;

import org.cqrs.springcqrs.dto.ProductDto;
import org.cqrs.springcqrs.entity.Product;
import org.cqrs.springcqrs.service.ProductCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

  @Autowired
  private ProductCommandService commandService;

  @PostMapping
  public Product createProduct(@RequestBody ProductDto productDto) {
    try {
      return commandService.createProduct(productDto);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @PutMapping("/{id}")
  public Product updateProduct(@PathVariable long id, @RequestBody ProductDto productDto) {

    try {
      return commandService.createProduct(productDto);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
}