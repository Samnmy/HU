package com.eventmanagement.application.usecase;

import com.eventmanagement.domain.model.User;
import com.eventmanagement.domain.model.UserRole;
import com.eventmanagement.domain.ports.in.AuthUseCase;
import com.eventmanagement.domain.ports.out.UserRepositoryPort;
import com.eventmanagement.application.dto.AuthRequest;
import com.eventmanagement.application.dto.AuthResponse;
import com.eventmanagement.application.dto.RegisterRequest;
import com.eventmanagement.domain.exception.DomainException;
import com.eventmanagement.infrastructure.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DomainException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DomainException("Email already registered");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken((UserDetails) savedUser);

        log.info("User registered successfully: {}", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new DomainException("User not found"));

        String token = jwtService.generateToken((UserDetails) user);

        log.info("User logged in successfully: {}", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void logout(String token) {
        jwtService.invalidateToken(token);
        log.info("User logged out");
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.isTokenValid(token);
    }
}