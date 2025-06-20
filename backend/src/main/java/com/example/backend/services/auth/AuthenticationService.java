package com.example.backend.services.auth;

import io.jsonwebtoken.Claims;

import java.util.Map;

public interface AuthenticationService {
    boolean authenticate(String email, String password);
    String generateToken(Map<String, Object> userInfo, long expirationTime);
    boolean validateToken(String token);
    Claims getClaimsFromToken(String token);
}
