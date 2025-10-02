package com.example.productmanagement.common.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    // Concrete implementation of the abstract BaseEntity for testing
    private static class TestEntity extends BaseEntity {
        private String name;
        
        public TestEntity() {
            super();
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }
    
    @Test
    void prePersist_WithNewEntity_ShouldSetIdAndTimestamps() {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setName("Test Entity");
        
        // Act
        entity.prePersist();
        
        // Assert
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getCreatedAt()).isEqualToIgnoringNanos(entity.getUpdatedAt());
    }
    
    @Test
    void prePersist_WithExistingEntity_ShouldUpdateOnlyUpdatedAt() {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setId("existing-id");
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        entity.setCreatedAt(createdAt);
        
        // Act
        entity.prePersist();
        
        // Assert
        assertThat(entity.getId()).isEqualTo("existing-id");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isAfter(entity.getCreatedAt());
    }
}
