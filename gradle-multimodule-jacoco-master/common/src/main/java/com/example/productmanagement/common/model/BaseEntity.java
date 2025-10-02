package com.example.productmanagement.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base entity class for all domain models
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public void prePersist() {
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
        
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        this.updatedAt = LocalDateTime.now();
    }
}
