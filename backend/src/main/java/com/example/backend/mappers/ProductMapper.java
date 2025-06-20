package com.example.backend.mappers;

import com.example.backend.entities.Product;
import com.example.backend.models.product.ProductDto;

public class ProductMapper {
    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .skuCode(product.getSkuCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .imageName(product.getImageName())
                .createdAt(product.getCreatedAt())
                .sellerId(product.getSeller().getId())
                .build();
    }
}
