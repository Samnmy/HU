package com.example.hexagonalapp.infrastructure.controllers;

import com.example.hexagonalapp.application.services.CategoryService;
import com.example.hexagonalapp.domain.models.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest request) {
    Category category = categoryService.createCategory(request.getName(), request.getDescription());
    return ResponseEntity.created(URI.create("/api/categories/" + category.getId())).body(category);
  }

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @GetMapping("/active")
  public ResponseEntity<List<Category>> getActiveCategories() {
    return ResponseEntity.ok(categoryService.getActiveCategories());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.getCategoryById(id));
  }

  @PutMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivateCategory(@PathVariable Long id) {
    categoryService.deactivateCategory(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/activate")
  public ResponseEntity<Void> activateCategory(@PathVariable Long id) {
    categoryService.activateCategory(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }

  public static class CreateCategoryRequest {
    private String name;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
  }
}
