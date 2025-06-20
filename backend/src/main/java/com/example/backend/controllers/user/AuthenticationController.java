package com.example.backend.controllers.user;

import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.user.AuthenticationRequest;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.auth.AuthenticationService;
import com.example.backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        // If the user is new, register will be triggered
        try {
            userService.getUserByEmail(userDto.getEmail());
            return ResponseEntity.badRequest().body("Email is already taken");
        } catch (UserNotFoundException e) { // No email taken
            userService.register(userDto);
            return ResponseEntity.ok("User registered successfully");
        }
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




}
