package com.eventmanagement.integration;

import com.eventmanagement.application.dto.AuthRequest;
import com.eventmanagement.application.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Setup data if needed
    }

    @Test
    void registerUser_ShouldReturnToken() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "testuser_" + System.currentTimeMillis(),
                "test" + System.currentTimeMillis() + "@test.com",
                "Test123",
                "ATTENDEE"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.token").isString())
                .andExpect(jsonPath("$.data.username").value(request.getUsername()))
                .andExpect(jsonPath("$.data.role").value("ATTENDEE"));
    }

    @Test
    void registerUser_WithDuplicateUsername_ShouldReturnError() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "duplicateuser",
                "unique1@test.com",
                "Test123",
                "ATTENDEE"
        );

        // First registration
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Second registration with same username
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Username already exists")));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // First register a user
        RegisterRequest registerRequest = new RegisterRequest(
                "loginuser",
                "login@test.com",
                "Password123",
                "ATTENDEE"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Then login
        AuthRequest loginRequest = new AuthRequest("loginuser", "Password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isString())
                .andExpect(jsonPath("$.data.username").value("loginuser"));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnError() throws Exception {
        AuthRequest request = new AuthRequest("nonexistent", "wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Invalid")));
    }

    @Test
    void login_WithEmptyCredentials_ShouldReturnValidationError() throws Exception {
        AuthRequest request = new AuthRequest("", "");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void logout_WithValidToken_ShouldSucceed() throws Exception {
        // Register and login to get token
        RegisterRequest registerRequest = new RegisterRequest(
                "logoutuser",
                "logout@test.com",
                "Password123",
                "ATTENDEE"
        );

        String registerResponse = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract token from response
        String token = extractToken(registerResponse);

        // Logout
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() throws Exception {
        // Register to get token
        RegisterRequest registerRequest = new RegisterRequest(
                "validateuser",
                "validate@test.com",
                "Password123",
                "ATTENDEE"
        );

        String registerResponse = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = extractToken(registerResponse);

        // Validate token
        mockMvc.perform(get("/api/v1/auth/validate")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() throws Exception {
        mockMvc.perform(get("/api/v1/auth/validate")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void protectedEndpoint_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk()); // This endpoint might be public

        // Test a protected endpoint
        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    private String extractToken(String jsonResponse) throws Exception {
        return objectMapper.readTree(jsonResponse)
                .path("data")
                .path("token")
                .asText();
    }
}