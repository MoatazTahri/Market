package com.example.backend.models.user;

import com.example.backend.enumerations.UserRole;
import com.example.backend.models.product.ProductDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    private Long id;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9]*$") // Not specified size in this pattern
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters") // Here making size limitation
    private String password;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private String phoneNumber;
    private String profilePictureName;
    private UserRole role;
    private boolean active;
    private boolean locked;
    private boolean expired;
    private Instant createdAt;
    private Set<ProductDto> products;
}
