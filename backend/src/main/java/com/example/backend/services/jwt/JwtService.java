package com.example.backend.services.jwt;

import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {
    /**
     * Generates a JWT token.
     *
     * @param claims Map of claims (e.g., user data) to include in the token.
     * @return Generated JWT token as a string.
     */
    String generateToken(Map<String, Object> claims, long expirationTime);

    /**
     * Validates a JWT token.
     *
     * @param token JWT token to validate.
     * @return true if the token is valid; false otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts claims from a JWT token.
     *
     * @param token JWT token.
     * @return Claims contained within the token.
     */
    Claims getClaims(String token);
}
