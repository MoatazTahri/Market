package com.example.backend.services.product;

import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.exceptions.ProductNotFoundException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.mappers.ProductMapper;
import com.example.backend.models.product.ProductDto;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addProduct(ProductDto productDto) {
        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new UserNotFoundException("Seller account not found"));
        Product product = Product.builder()
                .name(productDto.getName())
                .skuCode(productDto.getSkuCode())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .category(productDto.getCategory())
                .imageName(productDto.getImageName())
                .createdAt(Instant.now())
                .seller(seller)
                .build();
        productRepository.save(product);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setName(productDto.getName());
        product.setSkuCode(productDto.getSkuCode());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCategory(productDto.getCategory());
        product.setImageName(productDto.getImageName());
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::toProductDto).toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return ProductMapper.toProductDto(product);
    }
}
