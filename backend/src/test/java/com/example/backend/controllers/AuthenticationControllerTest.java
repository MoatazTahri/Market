package com.example.backend.controllers;

import com.example.backend.controllers.user.AuthenticationController;
import com.example.backend.enumerations.UserRole;
import com.example.backend.filter.RouteValidator;
import com.example.backend.models.user.AuthenticationRequest;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.auth.AuthenticationService;
import com.example.backend.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private RouteValidator routeValidator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnStatusCode200WhenUserRegister() throws Exception {
        // Registration with unique email
        UserDto mockUser = UserDto.builder()
                .email("test@gmail.com")
                .password("testtest")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+(905) 555 55 55")
                .profilePictureName("test.png")
                .build();
        when(userService.isEmailExists(any(String.class))).thenReturn(false);
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser))
        ).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusCode422WhenUserRegister() throws Exception {
        // Registration with unvalidated fields
        UserDto mockUser = UserDto.builder()
                .email("test@gmail.com")
                .password("")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+(905) 555 55 55")
                .profilePictureName("test.png")
                .build();
        when(userService.isEmailExists(any(String.class))).thenReturn(false);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser))
                ).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    public void shouldReturnStatusCode422WhenUserRegisterEmailExists() throws Exception {
        // Registration with existed email
        UserDto mockUser = UserDto.builder()
                .email("test@gmail.com")
                .password("testtest")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+(905) 555 55 55")
                .profilePictureName("test.png")
                .build();
        when(userService.isEmailExists(any(String.class))).thenReturn(true);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser))
                ).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.email").value("Email is already taken"));
    }

    @Test
    public void shouldReturnStatusCode200WhenUserLogin() throws Exception {
        // Login with good credentials
        AuthenticationRequest mockAuthRequest = AuthenticationRequest.builder()
                .email("test@gmail.com")
                .password("testtest")
                .build();
        UserDto mockUserDto = UserDto.builder()
                .email("test@gmail.com")
                .password("testtest")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+(905) 555 55 55")
                .profilePictureName("test.png")
                .role(UserRole.CUSTOMER)
                .refreshToken("test")
                .build();
        when(authenticationService.authenticate(any(String.class), any(String.class))).thenReturn(true);
        when(userService.getUserByEmail(any(String.class))).thenReturn(mockUserDto);
        when(authenticationService.generateToken(any(), any(Long.class))).thenReturn("test");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAuthRequest))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    public void shouldReturnStatusCode404WhenUserLogin() throws Exception {
        // Login with bad credentials
        AuthenticationRequest mockAuthRequest = AuthenticationRequest.builder()
                .email("test@gmail.com")
                .password("testtest")
                .build();
        UserDto mockUserDto = UserDto.builder()
                .email("test@gmail.com")
                .password("testtest")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+(905) 555 55 55")
                .profilePictureName("test.png")
                .role(UserRole.CUSTOMER)
                .refreshToken("test")
                .build();
        when(authenticationService.authenticate(any(String.class), any(String.class))).thenReturn(false);
        when(userService.getUserByEmail(any(String.class))).thenReturn(mockUserDto);
        when(authenticationService.generateToken(any(), any(Long.class))).thenReturn("test");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAuthRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid email or password"));
    }

    @Test
    public void shouldReturnStatusCode200WhenRefreshToken() throws Exception {
        // Refresh token with another valid token
        when(authenticationService.validateToken(any(String.class))).thenReturn(true);
        when(authenticationService.generateToken(any(), any(Long.class))).thenReturn("test");
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .param("refreshToken", "test")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").value("test"));
    }

    @Test
    public void shouldReturnStatusCode401WhenRefreshToken() throws Exception {
        // Refresh token with an invalidated token
        when(authenticationService.validateToken(any(String.class))).thenReturn(false);
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                .param("refreshToken", "test")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnStatusCode200WhenExtractClaims() throws Exception {
        // Extract claims from valid token
        Claims mockClaims = new DefaultClaims();
        mockClaims.put("test", "test");
        mockClaims.put("test2", "test2");
        when(authenticationService.validateToken(any(String.class))).thenReturn(true);
        when(authenticationService.getClaimsFromToken(any(String.class))).thenReturn(mockClaims);
        mockMvc.perform(get("/api/v1/auth/extract-claims")
                        .param("token", "test")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void shouldReturnStatusCode401WhenExtractClaims() throws Exception {
        // Extract claims from invalid token
        when(authenticationService.validateToken(any(String.class))).thenReturn(false);
        mockMvc.perform(get("/api/v1/auth/extract-claims")
                        .param("token", "test")
                ).andExpect(status().isUnauthorized());
    }
}
