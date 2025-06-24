package com.example.backend.entities;

import com.example.backend.enumerations.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(name = "pp_name")
    private String profilePictureName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String refreshToken;
    private boolean active;
    private boolean locked;
    private boolean expired;
    private Instant createdAt;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products;
}
