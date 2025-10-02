package com.example.productmanagement.application;

import com.example.productmanagement.products.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductManagementIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Using Redis as a simple container for demonstration
    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @Test
    void contextLoads() {
        // This test ensures that the Spring context loads successfully
        assertThat(redis.isRunning()).isTrue();
    }

    @Test
    void getAllProducts_ShouldReturnProducts() {
        // Arrange
        String url = "http://localhost:" + port + "/api/products";

        // Act
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void createAndGetProduct_ShouldCreateAndReturnProduct() {
        // Arrange
        String createUrl = "http://localhost:" + port + "/api/products";
        Product newProduct = createTestProduct();

        // Act - Create product
        ResponseEntity<Product> createResponse = restTemplate.postForEntity(
                createUrl,
                newProduct,
                Product.class
        );

        // Assert - Create response
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();
        
        // Get the created product ID
        String productId = createResponse.getBody().getId();
        
        // Arrange - Get product by ID
        String getUrl = "http://localhost:" + port + "/api/products/" + productId;
        
        // Act - Get product by ID
        ResponseEntity<Product> getResponse = restTemplate.getForEntity(
                getUrl,
                Product.class
        );
        
        // Assert - Get response
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(productId);
        assertThat(getResponse.getBody().getName()).isEqualTo(newProduct.getName());
    }

    private Product createTestProduct() {
        Set<String> categories = new HashSet<>();
        categories.add("Electronics");
        categories.add("Computers");
        
        return Product.builder()
                .name("Test Integration Product")
                .description("A product created in an integration test")
                .sku("TEST-INT-001")
                .price(new BigDecimal("299.99"))
                .quantityInStock(50)
                .categories(categories)
                .status(Product.ProductStatus.ACTIVE)
                .build();
    }
}
