package com.example.hexagonalapp.application.services;

import com.example.hexagonalapp.domain.models.User;
import com.example.hexagonalapp.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(String name, String email) {
    userRepository.findByEmail(email).ifPresent(user -> {
      throw new RuntimeException("El email ya está registrado");
    });

    User user = new User(name, email);
    return userRepository.save(user);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    return userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
  }

  public void deactivateUser(Long id) {
    User user = getUserById(id);
    user.deactivate();
    userRepository.save(user);
  }

  public void activateUser(Long id) {
    User user = getUserById(id);
    user.activate();
    userRepository.save(user);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
