package com.example.backend.controllers.product;

import com.example.backend.enumerations.ProductCategory;
import com.example.backend.models.product.ProductRequest;
import com.example.backend.models.product.ProductResponse;
import com.example.backend.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductRequest productRequest, BindingResult br) {
        // Global validation exception handler doesn't work with @ModelAttribute
        if (br.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            br.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        productService.addProduct(productRequest);
        return ResponseEntity.ok("Product added successfully");
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/all/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProductsByOwner(@PathVariable Long userId) {
        return productService.getProductsByOwner(userId);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductRequest productRequest) {
        productService.updateProduct(productRequest);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getProductCategories() {
        List<String> categories = new ArrayList<>();
        for (ProductCategory value : ProductCategory.values()) {
            categories.add(value.name());
        }
        return ResponseEntity.ok(categories);
    }
}
