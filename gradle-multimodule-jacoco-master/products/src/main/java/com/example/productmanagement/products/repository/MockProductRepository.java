package com.example.productmanagement.products.repository;

import com.example.productmanagement.products.model.Product;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Mock implementation of ProductRepository that uses in-memory storage
 */
public class MockProductRepository implements ProductRepository {
    
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final Faker faker = new Faker();
    
    public MockProductRepository() {
        // Initialize with some mock data
        initializeMockData();
    }
    
    private void initializeMockData() {
        // Create 10 random products
        for (int i = 0; i < 10; i++) {
            Product product = createMockProduct();
            products.put(product.getId(), product);
        }
    }
    
    private Product createMockProduct() {
        String id = UUID.randomUUID().toString();
        String name = faker.commerce().productName();
        String description = faker.lorem().paragraph();
        String sku = faker.code().isbn10();
        BigDecimal price = new BigDecimal(faker.commerce().price().replace(",", "."));
        int quantity = faker.random().nextInt(0, 100);
        
        Set<String> categories = new HashSet<>();
        categories.add(faker.commerce().department());
        if (faker.random().nextBoolean()) {
            categories.add(faker.commerce().department());
        }
        
        Product.ProductStatus status = Product.ProductStatus.values()[
            faker.random().nextInt(Product.ProductStatus.values().length)
        ];
        
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .sku(sku)
                .price(price)
                .quantityInStock(quantity)
                .categories(categories)
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
    
    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }
    
    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        
        product.setUpdatedAt(LocalDateTime.now());
        products.put(product.getId(), product);
        return product;
    }
    
    @Override
    public void deleteById(String id) {
        products.remove(id);
    }
    
    @Override
    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .filter(product -> product.getCategories() != null && 
                        product.getCategories().contains(category))
                .collect(Collectors.toList());
    }
}
