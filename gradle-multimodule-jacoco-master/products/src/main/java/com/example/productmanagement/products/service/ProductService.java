package com.example.productmanagement.products.service;

import com.example.productmanagement.products.model.Product;
import com.example.productmanagement.products.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing products
 */
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Get all products
     * 
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Get a product by ID
     * 
     * @param id the product ID
     * @return an Optional containing the product if found
     */
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }
    
    /**
     * Create a new product
     * 
     * @param product the product to create
     * @return the created product
     */
    public Product createProduct(Product product) {
        // Ensure this is a new product without an ID
        product.setId(null);
        return productRepository.save(product);
    }
    
    /**
     * Update an existing product
     * 
     * @param id the product ID
     * @param product the updated product data
     * @return the updated product or empty if not found
     */
    public Optional<Product> updateProduct(String id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Update the product data but keep the same ID
                    product.setId(existingProduct.getId());
                    product.setCreatedAt(existingProduct.getCreatedAt());
                    return productRepository.save(product);
                });
    }
    
    /**
     * Delete a product by ID
     * 
     * @param id the product ID
     * @return true if deleted, false if not found
     */
    public boolean deleteProduct(String id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get products by category
     * 
     * @param category the category to search for
     * @return a list of products in the category
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}
