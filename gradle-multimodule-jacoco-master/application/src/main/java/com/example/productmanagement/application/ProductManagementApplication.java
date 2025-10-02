package com.example.productmanagement.application;

import com.example.productmanagement.products.repository.MockProductRepository;
import com.example.productmanagement.products.repository.ProductRepository;
import com.example.productmanagement.products.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.productmanagement"})
public class ProductManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementApplication.class, args);
    }
    
    @Bean
    public ProductRepository productRepository() {
        return new MockProductRepository();
    }
    
    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
