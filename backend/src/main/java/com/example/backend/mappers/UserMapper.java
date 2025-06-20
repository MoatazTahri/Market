package com.example.backend.mappers;

import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.models.product.ProductDto;
import com.example.backend.models.user.UserDto;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        Set<ProductDto> productsDto = new HashSet<>();
        if (user.getProducts() != null) {
            for (Product product : user.getProducts()) {
                productsDto.add(ProductMapper.toProductDto(product));
            }
        }
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .locked(user.isLocked())
                .expired(user.isExpired())
                .active(user.isActive())
                .profilePictureName(user.getProfilePictureName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .products(productsDto)
                .build();
    }
}
