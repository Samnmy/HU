package com.example.hexagonalapp.domain.repositories;

import com.example.hexagonalapp.domain.models.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
  Product save(Product product);
  Optional<Product> findById(Long id);
  List<Product> findAll();
  List<Product> findByActiveTrue();
  void deleteById(Long id);
}
