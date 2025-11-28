package com.example.hexagonalapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  private Long id;
  private Long userId;
  private List<OrderItem> items;
  private BigDecimal totalAmount;
  private OrderStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Order(Long userId) {
    this.userId = userId;
    this.items = new ArrayList<>();
    this.totalAmount = BigDecimal.ZERO;
    this.status = OrderStatus.PENDING;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void addItem(Product product, Integer quantity) {
    if (quantity <= 0) {
      throw new RuntimeException("La cantidad debe ser mayor a 0");
    }

    items.stream()
      .filter(item -> item.getProductId().equals(product.getId()))
      .findFirst()
      .ifPresentOrElse(
        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + quantity),
        () -> items.add(new OrderItem(product.getId(), product.getName(), product.getPrice(), quantity))
      );

    recalculateTotal();
    this.updatedAt = LocalDateTime.now();
  }

  public void removeItem(Long productId) {
    items.removeIf(item -> item.getProductId().equals(productId));
    recalculateTotal();
    this.updatedAt = LocalDateTime.now();
  }

  public void updateItemQuantity(Long productId, Integer quantity) {
    if (quantity <= 0) {
      removeItem(productId);
      return;
    }

    items.stream()
      .filter(item -> item.getProductId().equals(productId))
      .findFirst()
      .ifPresent(item -> item.setQuantity(quantity));

    recalculateTotal();
    this.updatedAt = LocalDateTime.now();
  }

  private void recalculateTotal() {
    this.totalAmount = items.stream()
      .map(OrderItem::getSubtotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void confirm() {
    if (items.isEmpty()) {
      throw new RuntimeException("No se puede confirmar un pedido vacío");
    }
    this.status = OrderStatus.CONFIRMED;
    this.updatedAt = LocalDateTime.now();
  }

  public void cancel() {
    this.status = OrderStatus.CANCELLED;
    this.updatedAt = LocalDateTime.now();
  }

  public enum OrderStatus {
    PENDING, CONFIRMED, CANCELLED
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItem {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;

    public BigDecimal getSubtotal() {
      return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
  }
}
