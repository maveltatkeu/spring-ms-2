package com.example.productmanagement.products.repository;

import com.example.productmanagement.products.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entities
 */
public interface ProductRepository {
    /**
     * Find all products
     * 
     * @return a list of all products
     */
    List<Product> findAll();
    
    /**
     * Find a product by its ID
     * 
     * @param id the product ID
     * @return an Optional containing the product if found
     */
    Optional<Product> findById(String id);
    
    /**
     * Save a product
     * 
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);
    
    /**
     * Delete a product by its ID
     * 
     * @param id the product ID
     */
    void deleteById(String id);
    
    /**
     * Find products by category
     * 
     * @param category the category to search for
     * @return a list of products in the category
     */
    List<Product> findByCategory(String category);
}
