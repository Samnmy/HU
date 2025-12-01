package com.example.hexagonalapp.domain.repositories;

import com.example.hexagonalapp.domain.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
  Category save(Category category);
  Optional<Category> findById(Long id);
  List<Category> findAll();
  List<Category> findByActiveTrue();
  Optional<Category> findByName(String name);
  void deleteById(Long id);
}
