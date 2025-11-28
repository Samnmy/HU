package com.example.hexagonalapp.infrastructure.controllers;

import com.example.hexagonalapp.application.services.OrderService;
import com.example.hexagonalapp.domain.models.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
    Order order = orderService.createOrder(request.getUserId());
    return ResponseEntity.created(URI.create("/api/orders/" + order.getId())).body(order);
  }

  @PostMapping("/{orderId}/items")
  public ResponseEntity<Order> addItemToOrder(
    @PathVariable Long orderId,
    @RequestBody AddItemRequest request) {
    Order order = orderService.addItemToOrder(orderId, request.getProductId(), request.getQuantity());
    return ResponseEntity.ok(order);
  }

  @DeleteMapping("/{orderId}/items/{productId}")
  public ResponseEntity<Order> removeItemFromOrder(
    @PathVariable Long orderId,
    @PathVariable Long productId) {
    Order order = orderService.removeItemFromOrder(orderId, productId);
    return ResponseEntity.ok(order);
  }

  @PostMapping("/{orderId}/confirm")
  public ResponseEntity<Order> confirmOrder(@PathVariable Long orderId) {
    Order order = orderService.confirmOrder(orderId);
    return ResponseEntity.ok(order);
  }

  @PostMapping("/{orderId}/cancel")
  public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
    Order order = orderService.cancelOrder(orderId);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
    return ResponseEntity.ok(orderService.getOrdersByUser(userId));
  }

  public static class CreateOrderRequest {
    private Long userId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
  }

  public static class AddItemRequest {
    private Long productId;
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
  }
}
