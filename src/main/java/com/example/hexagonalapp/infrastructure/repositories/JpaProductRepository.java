package com.example.hexagonalapp.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
  List<ProductEntity> findByActiveTrue();
}
