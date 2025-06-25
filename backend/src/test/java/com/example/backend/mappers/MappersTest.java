package com.example.backend.mappers;

import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.enumerations.ProductCategory;
import com.example.backend.models.product.ProductResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MappersTest {

    @Test
    public void shouldMapProductToProductResponse() {
        User seller = new User();
        seller.setId(1L);
        seller.setEmail("test@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setName("Samsung TV");
        product.setSkuCode("TV-SM-001");
        product.setDescription("4K UHD Smart TV, 65 inches");
        product.setPrice(1200.0);
        product.setStock(50);
        product.setCategory(ProductCategory.TV);
        product.setImageName("samsung_tv_image.jpg");
        Instant now = Instant.now();
        product.setCreatedAt(now);
        product.setSeller(seller);

        ProductResponse productResponse = ProductMapper.toProductResponse(product);

        assertNotNull(productResponse);

        assertEquals(product.getId(), productResponse.getId());
        assertEquals(product.getName(), productResponse.getName());
        assertEquals(product.getSkuCode(), productResponse.getSkuCode());
        assertEquals(product.getDescription(), productResponse.getDescription());
        assertEquals(product.getPrice(), productResponse.getPrice());
        assertEquals(product.getStock(), productResponse.getStock());
        assertEquals(product.getCategory(), productResponse.getCategory());
        assertEquals("/uploads/products/" + product.getImageName(), productResponse.getImagePath());
        assertEquals(product.getCreatedAt(), productResponse.getCreatedAt());
        assertEquals(product.getSeller().getId(), productResponse.getSellerId());
    }
}
