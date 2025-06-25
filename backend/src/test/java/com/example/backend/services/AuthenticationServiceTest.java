package com.example.backend.services;

import com.example.backend.services.auth.AuthenticationServiceImpl;
import com.example.backend.services.jwt.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void shouldAuthenticate() {
        // Authentication success and stored
        String email = "test@gmail.com";
        String password = "testtest";
        Authentication authMock = mock(Authentication.class);
        when(authMock.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        boolean isAuthenticated = authenticationService.authenticate(email, password);
        // verify if auth manager get called only once
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        assertTrue(isAuthenticated);
        Authentication storedAuth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(storedAuth.isAuthenticated());

    }

    @Test
    public void shouldFailAuthenticate() {
        // Authentication must fail and not be stored
        String email = "test@gmail.com";
        String password = "testtest";
        Authentication authMock = mock(Authentication.class);
        when(authMock.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        boolean isAuthenticated = authenticationService.authenticate(email, password);
        assertFalse(isAuthenticated);
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        verify(jwtService, never()).generateToken(any(), any(Long.class));
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

    }

    @Test
    public void shouldGenerateToken() {
        // Generate token
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", "test@gmail.com");
        userInfo.put("role", "CUSTOMER");
        long expirationTime = 1000 * 60;
        String expectedToken = jwtService.generateToken(userInfo, expirationTime);
        when(jwtService.generateToken(userInfo, expirationTime)).thenReturn(expectedToken);
        String actualToken = authenticationService.generateToken(userInfo, expirationTime);
        assertEquals(expectedToken, actualToken);
        verify(jwtService, times(2)).generateToken(userInfo, expirationTime);
    }

    @Test
    public void shouldValidateToken() {
        // Token is valid
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", "test@gmail.com");
        userInfo.put("role", "CUSTOMER");
        long expirationTime = 1000 * 60;
        String givenToken = jwtService.generateToken(userInfo, expirationTime);
        when(jwtService.validateToken(givenToken)).thenReturn(true);
        boolean isTokenValid = authenticationService.validateToken(givenToken);
        assertTrue(isTokenValid);
        verify(jwtService, times(1)).validateToken(givenToken);
    }

    @Test
    public void shouldNotValidateToken() {
        // Token is invalid
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", "test@gmail.com");
        userInfo.put("role", "CUSTOMER");
        long expirationTime = 1000 * 60;
        String givenToken = jwtService.generateToken(userInfo, expirationTime);
        when(jwtService.validateToken(givenToken)).thenReturn(false);
        boolean isTokenValid = authenticationService.validateToken(givenToken);
        assertFalse(isTokenValid);
        verify(jwtService, times(1)).validateToken(givenToken);
    }
}
