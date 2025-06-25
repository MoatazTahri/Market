package com.example.backend.controllers;

import com.example.backend.controllers.product.ProductController;
import com.example.backend.enumerations.ProductCategory;
import com.example.backend.filter.RouteValidator;
import com.example.backend.models.product.ProductResponse;
import com.example.backend.services.auth.AuthenticationService;
import com.example.backend.services.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RouteValidator routeValidator;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private AuthenticationService authService;

    @Test
    public void shouldReturnStatusCode200WhenProductSubmitted() throws Exception {
        // Add product with valid fields
        MockMultipartFile mockedFile = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Spring Framework".getBytes());
        mockMvc.perform(multipart("/api/v1/product/create")
                .file(mockedFile)
                .param("name", "Test product")
                .param("price", "100")
                .param("skuCode", "sku-123")
                .param("stock", "10")
                .param("description", "Test description")
                .param("category", "TV")
                .param("sellerId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusCode422WhenProductSubmitted() throws Exception {
        // Add product with invalid fields
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Spring Framework".getBytes());
        mockMvc.perform(multipart("/api/v1/product/create")
                .file(mockFile)
                .param("name", "")
                .param("price", "")
                .param("skuCode", "sku-123")
                .param("stock", "10")
                .param("description", "Test description")
                .param("category", "TV")
                .param("sellerId", "1"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
        ;
    }

    @Test
    public void shouldReturnStatusCode200AndProductRetrieved() throws Exception {
        // Get product with id
        ProductResponse mockProduct = new ProductResponse(
                1L,
                "Laptop X1",
                "LAP-X1-SKU",
                "High-performance laptop",
                1200.0,
                50,
                ProductCategory.LAPTOP,
                "/uploads/products/abc.png",
                Instant.now(),
                1L
        );
        when(productService.getProductById(any(Long.class))).thenReturn(mockProduct);
        mockMvc.perform(get("/api/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.skuCode").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.sellerId").exists())
                .andExpect(jsonPath("$.imagePath").exists())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    public void shouldReturnStatusCode404WhenProductRetrieveNullId() throws Exception {
        // Get product with null id
        mockMvc.perform(get("/api/v1/product/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatusCode200AndProductLists() throws Exception {
        // Get all products
        ProductResponse mockProduct1 = new ProductResponse(
                1L,
                "Laptop X1",
                "LAP-X1-SKU",
                "High-performance laptop",
                1200.0,
                50,
                ProductCategory.LAPTOP,
                "/uploads/products/abc.png",
                Instant.now(),
                1L
        );
        ProductResponse mockProduct2 = new ProductResponse(
                2L,
                "Laptop X1",
                "LAP-X1-SKU",
                "High-performance laptop",
                1200.0,
                50,
                ProductCategory.LAPTOP,
                "/uploads/products/abc.png",
                Instant.now(),
                1L
        );
        when(productService.getAllProducts()).thenReturn(List.of(mockProduct1, mockProduct2));
        mockMvc.perform(get("/api/v1/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void shouldReturnStatusCode200WhenProductUpdate() throws Exception {
        // Update product with valid fields
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Spring Framework".getBytes()
        );
        mockMvc.perform(multipart("/api/v1/product/update")
                        .file(mockFile)
                        .param("name", "Test product")
                        .param("price", "100")
                        .param("skuCode", "sku-123")
                        .param("stock", "10")
                        .param("description", "Test description")
                        .param("category", "TV")
                        .param("sellerId", "1")
                        // Make multipart method as PUT
                        .with(request -> { request.setMethod("PUT"); return request; })
                )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusCode422WhenProductUpdate() throws Exception {
        // Update product with non-valid fields
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Spring Framework".getBytes()
        );
        mockMvc.perform(multipart("/api/v1/product/update")
                        .file(mockFile)
                        .param("name", "Test product")
                        .param("price", "100")
                        .param("skuCode", "")
                        .param("stock", "10")
                        .param("description", "Test description")
                        .param("category", "TV")
                        .param("sellerId", "1")
                        // Make multipart method as PUT
                        .with(request -> { request.setMethod("PUT"); return request; })
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.skuCode").exists());
    }

    @Test
    public void shouldReturnStatusCode200WhenProductDelete() throws Exception {
        // Delete product with id
        mockMvc.perform(delete("/api/v1/product/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusCode404WhenProductDeleteNullId() throws Exception {
        // Delete product with null id
        mockMvc.perform(delete("/api/v1/product/delete/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnArrayOfCategories() throws Exception {
        // Get all product categories
        mockMvc.perform(get("/api/v1/product/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
