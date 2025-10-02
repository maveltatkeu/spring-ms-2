package com.example.productmanagement.api.controller;

import com.example.productmanagement.products.model.Product;
import com.example.productmanagement.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;
    private String productId;

    @BeforeEach
    void setUp() {
        productId = "test-id";
        testProduct = createTestProduct(productId, "Test Product");
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(
                testProduct,
                createTestProduct("id2", "Product 2")
        );
        when(productService.getAllProducts()).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedProducts);
    }

    @Test
    void getProductById_WithExistingId_ShouldReturnProduct() {
        // Arrange
        when(productService.getProductById(productId)).thenReturn(Optional.of(testProduct));

        // Act
        ResponseEntity<Product> response = productController.getProductById(productId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testProduct);
    }

    @Test
    void getProductById_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        String nonExistingId = "non-existing-id";
        when(productService.getProductById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Product> response = productController.getProductById(nonExistingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        Product inputProduct = createTestProduct(null, "New Product");
        Product createdProduct = createTestProduct("new-id", "New Product");
        when(productService.createProduct(any(Product.class))).thenReturn(createdProduct);

        // Act
        ResponseEntity<Product> response = productController.createProduct(inputProduct);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdProduct);
    }

    @Test
    void updateProduct_WithExistingId_ShouldReturnUpdatedProduct() {
        // Arrange
        Product updatedProduct = createTestProduct(productId, "Updated Product");
        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        // Act
        ResponseEntity<Product> response = productController.updateProduct(productId, updatedProduct);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedProduct);
    }

    @Test
    void updateProduct_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        String nonExistingId = "non-existing-id";
        Product updatedProduct = createTestProduct(null, "Updated Product");
        when(productService.updateProduct(eq(nonExistingId), any(Product.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Product> response = productController.updateProduct(nonExistingId, updatedProduct);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteProduct_WithExistingId_ShouldReturnNoContent() {
        // Arrange
        when(productService.deleteProduct(productId)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(productId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteProduct_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        String nonExistingId = "non-existing-id";
        when(productService.deleteProduct(nonExistingId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(nonExistingId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getProductsByCategory_ShouldReturnMatchingProducts() {
        // Arrange
        String category = "Electronics";
        List<Product> expectedProducts = Arrays.asList(
                testProduct,
                createTestProduct("id2", "Product 2")
        );
        when(productService.getProductsByCategory(category)).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<Product>> response = productController.getProductsByCategory(category);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedProducts);
    }

    private Product createTestProduct(String id, String name) {
        Set<String> categories = new HashSet<>();
        categories.add("Electronics");
        
        return Product.builder()
                .id(id)
                .name(name)
                .description("Test Description")
                .sku("TEST-SKU-" + (id != null ? id : "new"))
                .price(new BigDecimal("99.99"))
                .quantityInStock(10)
                .categories(categories)
                .status(Product.ProductStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
