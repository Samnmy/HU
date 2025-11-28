package com.example.hexagonalapp.infrastructure.repositories;

import com.example.hexagonalapp.domain.models.Product;
import com.example.hexagonalapp.domain.repositories.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

  private final JpaProductRepository jpaProductRepository;

  public ProductRepositoryAdapter(JpaProductRepository jpaProductRepository) {
    this.jpaProductRepository = jpaProductRepository;
  }

  private ProductEntity toEntity(Product product) {
    return new ProductEntity(
      product.getId(),
      product.getName(),
      product.getDescription(),
      product.getPrice(),
      product.getStock(),
      product.getCreatedAt(),
      product.isActive()
    );
  }

  private Product toDomain(ProductEntity entity) {
    return new Product(
      entity.getId(),
      entity.getName(),
      entity.getDescription(),
      entity.getPrice(),
      entity.getStock(),
      entity.getCreatedAt(),
      entity.isActive()
    );
  }

  @Override
  public Product save(Product product) {
    ProductEntity entity = toEntity(product);
    ProductEntity savedEntity = jpaProductRepository.save(entity);
    return toDomain(savedEntity);
  }

  @Override
  public Optional<Product> findById(Long id) {
    return jpaProductRepository.findById(id)
      .map(this::toDomain);
  }

  @Override
  public List<Product> findAll() {
    return jpaProductRepository.findAll().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Product> findByActiveTrue() {
    return jpaProductRepository.findByActiveTrue().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaProductRepository.deleteById(id);
  }
}
