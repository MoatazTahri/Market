package com.example.backend.repository;

import com.example.backend.entities.Product;
import com.example.backend.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldSaveAndFindProduct() {
        Product mockProduct = new Product();
        mockProduct.setName("Samsung TV");
        mockProduct.setSkuCode("SMSNG-L0001");
        productRepository.save(mockProduct);
        Optional<Product> foundProduct = productRepository.findById(mockProduct.getId());
        assertTrue(foundProduct.isPresent());
        assertEquals(foundProduct.get().getSkuCode(), mockProduct.getSkuCode());
    }
}
