package com.example.hexagonalapp.application.services;

import com.example.hexagonalapp.domain.models.Category;
import com.example.hexagonalapp.domain.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional
  public Category createCategory(String name, String description) {
    Category category = new Category(name, description);
    return categoryRepository.save(category);
  }

  @Transactional(readOnly = true)
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Category> getActiveCategories() {
    return categoryRepository.findByActiveTrue();
  }

  @Transactional(readOnly = true)
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
  }

  @Transactional
  public void deactivateCategory(Long id) {
    Category category = getCategoryById(id);
    category.deactivate();
    categoryRepository.save(category);
  }

  @Transactional
  public void activateCategory(Long id) {
    Category category = getCategoryById(id);
    category.activate();
    categoryRepository.save(category);
  }

  @Transactional
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }
}
