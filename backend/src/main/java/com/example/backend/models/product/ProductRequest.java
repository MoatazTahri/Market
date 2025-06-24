package com.example.backend.models.product;

import com.example.backend.enumerations.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductRequest {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "SKU code is required")
    private String skuCode;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than 0")
    private Integer stock;
    @NotNull(message = "Category is required")
    private ProductCategory category;
    @NotNull(message = "Image is required")
    private MultipartFile image;
    private Long sellerId;
}
