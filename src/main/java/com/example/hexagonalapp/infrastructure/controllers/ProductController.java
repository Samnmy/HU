package com.example.hexagonalapp.infrastructure.controllers;

import com.example.hexagonalapp.application.services.ProductService;
import com.example.hexagonalapp.domain.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
    Product product = productService.createProduct(
      request.getName(),
      request.getDescription(),
      request.getPrice(),
      request.getStock()
    );
    return ResponseEntity.created(URI.create("/api/products/" + product.getId())).body(product);
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/active")
  public ResponseEntity<List<Product>> getActiveProducts() {
    return ResponseEntity.ok(productService.getActiveProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @PostMapping("/{id}/stock")
  public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestBody UpdateStockRequest request) {
    Product product = productService.updateStock(id, request.getQuantity());
    return ResponseEntity.ok(product);
  }

  @PostMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
    productService.deactivateProduct(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/activate")
  public ResponseEntity<Void> activateProduct(@PathVariable Long id) {
    productService.activateProduct(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  public static class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
  }

  public static class UpdateStockRequest {
    private Integer quantity;

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
  }
}
