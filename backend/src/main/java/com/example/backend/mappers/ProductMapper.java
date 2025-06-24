package com.example.backend.mappers;

import com.example.backend.entities.Product;
import com.example.backend.models.product.ProductRequest;
import com.example.backend.models.product.ProductResponse;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProductMapper {
    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .skuCode(product.getSkuCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .imagePath("/uploads/products/" + product.getImageName())
                .createdAt(product.getCreatedAt())
                .sellerId(product.getSeller().getId())
                .build();
    }
}
