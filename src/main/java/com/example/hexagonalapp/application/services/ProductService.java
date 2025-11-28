package com.example.hexagonalapp.application.services;

import com.example.hexagonalapp.domain.models.Product;
import com.example.hexagonalapp.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product createProduct(String name, String description, BigDecimal price, Integer stock) {
    Product product = new Product(name, description, price, stock);
    return productRepository.save(product);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public List<Product> getActiveProducts() {
    return productRepository.findByActiveTrue();
  }

  public Product getProductById(Long id) {
    return productRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
  }

  public Product updateStock(Long id, Integer quantity) {
    Product product = getProductById(id);
    product.updateStock(quantity);
    return productRepository.save(product);
  }

  public void deactivateProduct(Long id) {
    Product product = getProductById(id);
    product.deactivate();
    productRepository.save(product);
  }

  public void activateProduct(Long id) {
    Product product = getProductById(id);
    product.activate();
    productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}
