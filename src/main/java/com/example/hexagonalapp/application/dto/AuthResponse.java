package com.example.hexagonalapp.application.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private java.util.Set<String> roles;
}