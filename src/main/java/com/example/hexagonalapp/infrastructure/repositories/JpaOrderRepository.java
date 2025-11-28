package com.example.hexagonalapp.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
  List<OrderEntity> findByUserId(Long userId);
}
