package com.eventmanagement.domain.ports.in;

import com.eventmanagement.application.dto.AuthRequest;
import com.eventmanagement.application.dto.AuthResponse;
import com.eventmanagement.application.dto.RegisterRequest;

public interface AuthUseCase {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    void logout(String token);
    boolean validateToken(String token);
}