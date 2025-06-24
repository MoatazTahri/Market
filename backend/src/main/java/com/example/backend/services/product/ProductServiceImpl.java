package com.example.backend.services.product;

import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.exceptions.ProductNotFoundException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.mappers.ProductMapper;
import com.example.backend.models.product.ProductRequest;
import com.example.backend.models.product.ProductResponse;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.tools.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addProduct(ProductRequest productRequest) {
        User seller = userRepository.findById(productRequest.getSellerId())
                .orElseThrow(() -> new UserNotFoundException("Seller account not found"));
        Path uploadDir = Paths.get("uploads/products");
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String codedImageName = FileUtil.codeFileName(productRequest.getImage().getOriginalFilename());
            Path imagePath = uploadDir.resolve(codedImageName);
            Files.copy(productRequest.getImage().getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            Product product = Product.builder()
                    .name(productRequest.getName())
                    .skuCode(productRequest.getSkuCode())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .stock(productRequest.getStock())
                    .category(productRequest.getCategory())
                    .imageName(codedImageName)
                    .createdAt(Instant.now())
                    .seller(seller)
                    .build();
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("Could not save product image. " + e);
        }
    }

    @Transactional
    @Override
    public void updateProduct(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Path uploadDir = Paths.get("uploads/products");
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Files.deleteIfExists(uploadDir.resolve(product.getImageName()));
            String codedImageName = FileUtil.codeFileName(productRequest.getImage().getOriginalFilename());
            Path imagePath = uploadDir.resolve(codedImageName);
            Files.copy(productRequest.getImage().getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            product.setName(productRequest.getName());
            product.setSkuCode(productRequest.getSkuCode());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            product.setCategory(productRequest.getCategory());
            product.setImageName(codedImageName);
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("Could not save product image. " + e);
        }
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Path uploadDir = Paths.get("uploads/products");
        try {
            Files.deleteIfExists(uploadDir.resolve(product.getImageName()));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete product image. " + e);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return ProductMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByOwner(Long userId) {
        return productRepository.findAllBySellerId(userId)
                .stream()
                .map(ProductMapper::toProductResponse)
                .toList();
    }
}
