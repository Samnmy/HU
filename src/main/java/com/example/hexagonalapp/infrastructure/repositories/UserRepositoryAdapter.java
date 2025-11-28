package com.example.hexagonalapp.infrastructure.repositories;

import com.example.hexagonalapp.domain.models.User;
import com.example.hexagonalapp.domain.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements UserRepository {

  private final JpaUserRepository jpaUserRepository;

  public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
    this.jpaUserRepository = jpaUserRepository;
  }

  private UserEntity toEntity(User user) {
    return new UserEntity(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getCreatedAt(),
      user.isActive()
    );
  }

  private User toDomain(UserEntity entity) {
    return new User(
      entity.getId(),
      entity.getName(),
      entity.getEmail(),
      entity.getCreatedAt(),
      entity.isActive()
    );
  }

  @Override
  public User save(User user) {
    UserEntity entity = toEntity(user);
    UserEntity savedEntity = jpaUserRepository.save(entity);
    return toDomain(savedEntity);
  }

  @Override
  public Optional<User> findById(Long id) {
    return jpaUserRepository.findById(id)
      .map(this::toDomain);
  }

  @Override
  public List<User> findAll() {
    return jpaUserRepository.findAll().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaUserRepository.deleteById(id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpaUserRepository.findByEmail(email)
      .map(this::toDomain);
  }
}
