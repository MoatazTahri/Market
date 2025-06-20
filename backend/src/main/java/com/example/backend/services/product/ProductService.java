package com.example.backend.services.product;

import com.example.backend.models.product.ProductDto;

import java.util.List;

public interface ProductService {
    /**
     * Adds a new product to the system.
     *
     * @param productDto the product to be added
     */
    void addProduct(ProductDto productDto);

    /**
     * Updates an existing product.
     *
     * @param productDto the product with updated information
     */
    void updateProduct(ProductDto productDto);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    void deleteProductById(Long id);

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    List<ProductDto> getAllProducts();

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the given ID
     */
    ProductDto getProductById(Long id);
}
