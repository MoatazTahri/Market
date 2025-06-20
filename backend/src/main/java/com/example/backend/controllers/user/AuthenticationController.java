package com.example.backend.controllers.user;

import com.example.backend.models.user.AuthenticationRequest;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        if (userService.getUserByEmail(userDto.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticationRequest request) {
        if (userService.login(request.getEmail(), request.getPassword())) {
            return ResponseEntity.ok(userService.getUserByEmail(request.getEmail()));
        } else {
            return ResponseEntity.badRequest().body("Email or password is incorrect");
        }
    }

}
