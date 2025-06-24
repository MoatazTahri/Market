package com.example.backend.controllers.user;

import com.example.backend.enumerations.UserRole;
import com.example.backend.models.user.AuthenticationRequest;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.auth.AuthenticationService;
import com.example.backend.services.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes.
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 60L * 60 * 24 * 30 * 1000; // 30 days

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        // Giving it the same response status as fields validation.
        if (userService.isEmailExists(userDto.getEmail())) {
            return new ResponseEntity<>(Map.of("email", "Email is already taken"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Generate refresh token to insert in database
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", userDto.getEmail());
        userInfo.put("role", UserRole.CUSTOMER);
        userDto.setRefreshToken(authService.generateToken(userInfo, REFRESH_TOKEN_EXPIRATION_TIME));
        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticationRequest request) {
        boolean isAuthenticated = authService.authenticate(request.getEmail(), request.getPassword());
        if (isAuthenticated) {
            Map<String, Object> userInfo = new HashMap<>();
            UserDto userDto = userService.getUserByEmail(request.getEmail());
            userInfo.put("email", userDto.getEmail());
            userInfo.put("role", userDto.getRole().name());
            String accessToken = authService.generateToken(userInfo, ACCESS_TOKEN_EXPIRATION_TIME);
            String refreshToken = userDto.getRefreshToken();
            return new ResponseEntity<>(
                    Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken)
                    , HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestParam String refreshToken) {
        try {
            if (!authService.validateToken(refreshToken)) {
                return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
            }
            Claims claims = authService.getClaimsFromToken(refreshToken);
            String newAccessToken = authService.generateToken(claims, ACCESS_TOKEN_EXPIRATION_TIME);
            return ResponseEntity.ok(newAccessToken);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Refresh token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/extract-claims")
    public ResponseEntity<Object> getClaims(@RequestParam String token) {
        try {
            if (!authService.validateToken(token)) {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
            Claims claims = authService.getClaimsFromToken(token);
            return ResponseEntity.ok(claims);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }


}
