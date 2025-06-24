package com.example.backend.models.product;

import com.example.backend.enumerations.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private double price;
    private int stock;
    private ProductCategory category;
    private String imagePath;
    private Instant createdAt;
    private Long sellerId;
}
