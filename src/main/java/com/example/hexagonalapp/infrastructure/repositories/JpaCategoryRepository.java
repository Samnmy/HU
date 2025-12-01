package com.example.hexagonalapp.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
  List<CategoryEntity> findByActiveTrue();
  Optional<CategoryEntity> findByName(String name);
}
