package com.example.hexagonalapp.infrastructure.repositories;

import com.example.hexagonalapp.domain.models.Category;
import com.example.hexagonalapp.domain.repositories.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryAdapter implements CategoryRepository {

  private final JpaCategoryRepository jpaCategoryRepository;

  public CategoryRepositoryAdapter(JpaCategoryRepository jpaCategoryRepository) {
    this.jpaCategoryRepository = jpaCategoryRepository;
  }

  private CategoryEntity toEntity(Category category) {
    CategoryEntity entity = new CategoryEntity();
    entity.setId(category.getId());
    entity.setName(category.getName());
    entity.setDescription(category.getDescription());
    entity.setActive(category.isActive());
    entity.setCreatedAt(category.getCreatedAt());
    return entity;
  }

  private Category toDomain(CategoryEntity entity) {
    return new Category(
      entity.getId(),
      entity.getName(),
      entity.getDescription(),
      entity.isActive(),
      entity.getCreatedAt(),
      null
    );
  }

  @Override
  public Category save(Category category) {
    CategoryEntity entity = toEntity(category);
    CategoryEntity savedEntity = jpaCategoryRepository.save(entity);
    return toDomain(savedEntity);
  }

  @Override
  public Optional<Category> findById(Long id) {
    return jpaCategoryRepository.findById(id)
      .map(this::toDomain);
  }

  @Override
  public List<Category> findAll() {
    return jpaCategoryRepository.findAll().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Category> findByActiveTrue() {
    return jpaCategoryRepository.findByActiveTrue().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<Category> findByName(String name) {
    return jpaCategoryRepository.findByName(name)
      .map(this::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    jpaCategoryRepository.deleteById(id);
  }
}
