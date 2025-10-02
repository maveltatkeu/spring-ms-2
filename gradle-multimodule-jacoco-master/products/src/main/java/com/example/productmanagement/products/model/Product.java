package com.example.productmanagement.products.model;

import com.example.productmanagement.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Product entity model
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private int quantityInStock;
    private Set<String> categories;
    private ProductStatus status;
    
    public enum ProductStatus {
        ACTIVE,
        DISCONTINUED,
        OUT_OF_STOCK
    }
    
    public boolean isAvailable() {
        return status == ProductStatus.ACTIVE && quantityInStock > 0;
    }
}
