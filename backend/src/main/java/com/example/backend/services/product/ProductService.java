package com.example.backend.services.product;

import com.example.backend.models.product.ProductRequest;
import com.example.backend.models.product.ProductResponse;

import java.util.List;

public interface ProductService {
    /**
     * Adds a new product to the system.
     *
     * @param productRequest the product to be added
     */
    void addProduct(ProductRequest productRequest);

    /**
     * Updates an existing product.
     *
     * @param productRequest the product with updated information
     */
    void updateProduct(ProductRequest productRequest);

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
    List<ProductResponse> getAllProducts();

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the given ID
     */
    ProductResponse getProductById(Long id);

    /**
     * Retrieves all products by the owner ID.
     *
     * @param userId the ID of the user to retrieve their products.
     * @return list of products with the given user ID
     */
    List<ProductResponse> getProductsByOwner(Long userId);
}
