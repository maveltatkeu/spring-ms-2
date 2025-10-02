package com.example.productmanagement.products.service;

import com.example.productmanagement.products.model.Product;
import com.example.productmanagement.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(
                createTestProduct("1", "Product 1"),
                createTestProduct("2", "Product 2")
        );
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertThat(result).isEqualTo(expectedProducts);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_WithExistingId_ShouldReturnProduct() {
        // Arrange
        String id = "test-id";
        Product expectedProduct = createTestProduct(id, "Test Product");
        when(productRepository.findById(id)).thenReturn(Optional.of(expectedProduct));

        // Act
        Optional<Product> result = productService.getProductById(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedProduct);
        verify(productRepository).findById(id);
    }

    @Test
    void getProductById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        String id = "non-existing-id";
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.getProductById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(productRepository).findById(id);
    }

    @Test
    void createProduct_ShouldSaveProductWithNewId() {
        // Arrange
        Product inputProduct = createTestProduct("original-id", "New Product");
        Product savedProduct = createTestProduct("new-id", "New Product");
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(inputProduct);

        // Assert
        assertThat(result).isEqualTo(savedProduct);
        verify(productRepository).save(argThat(product -> 
                product.getId() == null && 
                "New Product".equals(product.getName())));
    }

    @Test
    void updateProduct_WithExistingId_ShouldUpdateAndReturnProduct() {
        // Arrange
        String id = "existing-id";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        
        Product existingProduct = createTestProduct(id, "Old Name");
        existingProduct.setCreatedAt(createdAt);
        
        Product updatedData = createTestProduct(null, "Updated Name");
        Product savedProduct = createTestProduct(id, "Updated Name");
        savedProduct.setCreatedAt(createdAt);
        
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Optional<Product> result = productService.updateProduct(id, updatedData);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedProduct);
        verify(productRepository).findById(id);
        verify(productRepository).save(argThat(product -> 
                id.equals(product.getId()) && 
                "Updated Name".equals(product.getName()) &&
                createdAt.equals(product.getCreatedAt())));
    }

    @Test
    void updateProduct_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        String id = "non-existing-id";
        Product updatedData = createTestProduct(null, "Updated Name");
        
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.updateProduct(id, updatedData);

        // Assert
        assertThat(result).isEmpty();
        verify(productRepository).findById(id);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_WithExistingId_ShouldDeleteAndReturnTrue() {
        // Arrange
        String id = "existing-id";
        Product existingProduct = createTestProduct(id, "Product to Delete");
        
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).deleteById(id);

        // Act
        boolean result = productService.deleteProduct(id);

        // Assert
        assertThat(result).isTrue();
        verify(productRepository).findById(id);
        verify(productRepository).deleteById(id);
    }

    @Test
    void deleteProduct_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        String id = "non-existing-id";
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = productService.deleteProduct(id);

        // Assert
        assertThat(result).isFalse();
        verify(productRepository).findById(id);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void getProductsByCategory_ShouldReturnMatchingProducts() {
        // Arrange
        String category = "Electronics";
        List<Product> expectedProducts = Arrays.asList(
                createTestProduct("1", "Product 1"),
                createTestProduct("2", "Product 2")
        );
        when(productRepository.findByCategory(category)).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getProductsByCategory(category);

        // Assert
        assertThat(result).isEqualTo(expectedProducts);
        verify(productRepository).findByCategory(category);
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
