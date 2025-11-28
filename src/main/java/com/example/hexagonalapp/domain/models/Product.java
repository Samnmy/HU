package com.example.hexagonalapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer stock;
  private LocalDateTime createdAt;
  private boolean active;

  public Product(String name, String description, BigDecimal price, Integer stock) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.createdAt = LocalDateTime.now();
    this.active = true;
  }

  public void updateStock(Integer quantity) {
    if (this.stock + quantity < 0) {
      throw new RuntimeException("Stock insuficiente");
    }
    this.stock += quantity;
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }
}
