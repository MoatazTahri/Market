package com.example.backend.controllers.user;

import com.example.backend.models.user.AuthenticationRequest;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.auth.AuthenticationService;
import com.example.backend.services.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        // Giving it the same response status as fields validation.
        if (userService.isEmailExists(userDto.getEmail())) {
            return new ResponseEntity<>(Map.of("email","Email is already taken"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticationRequest request) {
        boolean isAuthenticated = authService.authenticate(request.getEmail(), request.getPassword());
        long expirationTime = 60 * 60 * 1000; // 1 hour
        if (isAuthenticated) {
            Map<String, Object> userInfo = new HashMap<>();
            UserDto userDto = userService.getUserByEmail(request.getEmail());
            userInfo.put("email", userDto.getEmail());
            userInfo.put("role",  userDto.getRole().name());
            String token = authService.generateToken(userInfo, expirationTime);
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    @GetMapping("/claims")
    public ResponseEntity<Object> getClaims(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Claims claims = authService.getClaimsFromToken(token);
        return ResponseEntity.ok(claims);
    }



}
