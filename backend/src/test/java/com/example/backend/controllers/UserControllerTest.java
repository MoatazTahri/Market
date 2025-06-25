package com.example.backend.controllers;

import com.example.backend.controllers.user.UserController;
import com.example.backend.enumerations.UserRole;
import com.example.backend.filter.RouteValidator;
import com.example.backend.models.product.ProductResponse;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.auth.AuthenticationService;
import com.example.backend.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationService authService;

    @MockitoBean
    private RouteValidator routeValidator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnStatusCode200WhenGetUserAndRetrieveIt() throws Exception {
        // Get user by id
        UserDto mockUser = UserDto.builder()
                .id(1L)
                .email("mock.test@example.com")
                .password("SecureP@ssw0rd1")
                .firstName("Mock")
                .lastName("User")
                .phoneNumber("111-222-3333")
                .profilePictureName("mock_profile.png")
                .role(UserRole.CUSTOMER)
                .refreshToken("some_refresh_token_string")
                .active(true)
                .locked(false)
                .expired(false)
                .createdAt(Instant.now())
                .products(Set.of(new ProductResponse()))
                .build();
        when(userService.getUserById(any(Long.class))).thenReturn(mockUser);
        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.phoneNumber").exists())
                .andExpect(jsonPath("$.profilePictureName").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.active").exists())
                .andExpect(jsonPath("$.locked").exists())
                .andExpect(jsonPath("$.expired").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.products").isArray());
    }

    @Test
    public void shouldReturnStatusCode200WhenFindUserByEmailAndRetrieveIt() throws Exception {
        // Get user by email
        UserDto mockUser = UserDto.builder()
                .id(1L)
                .email("mock.test@example.com")
                .password("SecureP@ssw0rd1")
                .firstName("Mock")
                .lastName("User")
                .phoneNumber("111-222-3333")
                .profilePictureName("mock_profile.png")
                .role(UserRole.CUSTOMER)
                .refreshToken("some_refresh_token_string")
                .active(true)
                .locked(false)
                .expired(false)
                .createdAt(Instant.now())
                .products(Set.of(new ProductResponse()))
                .build();
        when(userService.getUserByEmail(any(String.class))).thenReturn(mockUser);
        mockMvc.perform(get("/api/v1/user/find")
                        .param("email", "test@gmail.com")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.phoneNumber").exists())
                .andExpect(jsonPath("$.profilePictureName").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.active").exists())
                .andExpect(jsonPath("$.locked").exists())
                .andExpect(jsonPath("$.expired").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.products").isArray());
    }

    @Test
    public void shouldReturnStatusCode200WhenNotFindUserByEmail() throws Exception {
        // Get user when email does not exist
        when(userService.getUserByEmail(any(String.class))).thenReturn(null);
        mockMvc.perform(get("/api/v1/user/find")
                        .param("email", "test@gmail.com")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnStatusCode200WhenCheckEmailExists() throws Exception {
        // Check for email when does exist
        when(userService.isEmailExists(any(String.class))).thenReturn(true);
        mockMvc.perform(get("/api/v1/user/check-email")
                        .param("email", "test@gmail.com")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void shouldReturnStatusCode200WhenCheckEmailUnexist() throws Exception {
        // Check for email when does not exist
        when(userService.isEmailExists(any(String.class))).thenReturn(false);
        mockMvc.perform(get("/api/v1/user/check-email")
                        .param("email", "test@gmail.com")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"));
    }

    @Test
    public void shouldReturnStatusCode200WhenGetAllUsersAndRetrieveThem() throws Exception {
        // Get all users
        when(userService.getAllUsers()).thenReturn(List.of(new UserDto()));
        mockMvc.perform(get("/api/v1/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void shouldReturnStatusCode200WhenUpdateUser() throws Exception {
        // Update user with valid fields
        UserDto mockUser = UserDto.builder()
                .email("test@gmail.com")
                .password("testtest")
                .firstName("Test")
                .lastName("User")
                .build();
        mockMvc.perform(post("/api/v1/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser))
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                })
        ).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusCode422WhenUpdateUser() throws Exception {
        // Update user with invalid fields
        UserDto mockUser = UserDto.builder()
                .email("test@gmail.com")
                .password("te") // 2 characters < 8
                .firstName("Test")
                .lastName("User")
                .build();
        mockMvc.perform(post("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser))
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                ).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    public void shouldReturnStatusCode200WhenUserDelete() throws Exception {
        // Delete user with id
        mockMvc.perform(delete("/api/v1/user/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusCode404WhenUserDeleteNullId() throws Exception {
        // Delete user with null id
        mockMvc.perform(delete("/api/v1/user/delete/"))
                .andExpect(status().isNotFound());
    }
}
