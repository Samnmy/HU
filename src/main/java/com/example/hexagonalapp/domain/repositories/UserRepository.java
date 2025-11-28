package com.example.hexagonalapp.domain.repositories;

import com.example.hexagonalapp.domain.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  User save(User user);
  Optional<User> findById(Long id);
  List<User> findAll();
  void deleteById(Long id);
  Optional<User> findByEmail(String email);
}
