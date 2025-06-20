package com.example.backend.models.product;

import com.example.backend.enumerations.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDto {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "SKU code is required")
    private String skuCode;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    private double price;
    @NotNull(message = "Stock is required")
    private int stock;
    @NotNull(message = "Category is required")
    private ProductCategory category;
    @NotBlank(message = "Image name is required")
    private String imageName;
    private Instant createdAt;
    private Long sellerId;
}
