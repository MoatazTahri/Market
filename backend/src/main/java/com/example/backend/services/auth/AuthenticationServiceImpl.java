package com.example.backend.services.auth;

import com.example.backend.services.jwt.JwtService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public boolean authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            return authentication.isAuthenticated();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateToken(Map<String, Object> userInfo, long expirationTime) {
        return jwtService.generateToken(userInfo, expirationTime);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return jwtService.getClaims(token);
    }
}
