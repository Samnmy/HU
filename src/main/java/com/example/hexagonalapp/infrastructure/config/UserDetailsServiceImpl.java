package com.example.hexagonalapp.infrastructure.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // En producción, esto vendría de una base de datos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Usuarios hardcodeados para demostración
        if ("admin".equals(username)) {
            return new User(
                    "admin",
                    "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV6UiC", // password: admin123
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        } else if ("user".equals(username)) {
            return new User(
                    "user",
                    "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV6UiC", // password: user123
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}