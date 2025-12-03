package com.example.hexagonalapp.domain.model;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}