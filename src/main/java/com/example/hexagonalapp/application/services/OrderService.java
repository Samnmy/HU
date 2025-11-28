package com.example.hexagonalapp.application.services;

import com.example.hexagonalapp.domain.models.Order;
import com.example.hexagonalapp.domain.models.Product;
import com.example.hexagonalapp.domain.models.User;
import com.example.hexagonalapp.domain.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserService userService;
  private final ProductService productService;

  public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService) {
    this.orderRepository = orderRepository;
    this.userService = userService;
    this.productService = productService;
  }

  public Order createOrder(Long userId) {

    User user = userService.getUserById(userId);
    if (!user.isActive()) {
      throw new RuntimeException("El usuario no está activo");
    }

    Order order = new Order(userId);
    return orderRepository.save(order);
  }

  @Transactional
  public Order addItemToOrder(Long orderId, Long productId, Integer quantity) {
    Order order = getOrderById(orderId);


    if (order.getStatus() != Order.OrderStatus.PENDING) {
      throw new RuntimeException("No se puede modificar un pedido confirmado o cancelado");
    }

    Product product = productService.getProductById(productId);

    if (!product.isActive()) {
      throw new RuntimeException("El producto no está activo");
    }

    if (product.getStock() < quantity) {
      throw new RuntimeException("Stock insuficiente. Stock disponible: " + product.getStock());
    }

    order.addItem(product, quantity);


    productService.updateStock(productId, -quantity);

    return orderRepository.save(order);
  }

  public Order removeItemFromOrder(Long orderId, Long productId) {
    Order order = getOrderById(orderId);


    if (order.getStatus() != Order.OrderStatus.PENDING) {
      throw new RuntimeException("No se puede modificar un pedido confirmado o cancelado");
    }


    Order.OrderItem itemToRemove = order.getItems().stream()
      .filter(item -> item.getProductId().equals(productId))
      .findFirst()
      .orElseThrow(() -> new RuntimeException("Item no encontrado en el pedido"));


    productService.updateStock(productId, itemToRemove.getQuantity());

    order.removeItem(productId);
    return orderRepository.save(order);
  }

  @Transactional
  public Order confirmOrder(Long orderId) {
    Order order = getOrderById(orderId);

    if (order.getItems().isEmpty()) {
      throw new RuntimeException("No se puede confirmar un pedido vacío");
    }

    if (order.getStatus() != Order.OrderStatus.PENDING) {
      throw new RuntimeException("Solo se pueden confirmar pedidos pendientes");
    }

    order.confirm();
    return orderRepository.save(order);
  }

  @Transactional
  public Order cancelOrder(Long orderId) {
    Order order = getOrderById(orderId);

    if (order.getStatus() != Order.OrderStatus.PENDING) {
      throw new RuntimeException("Solo se pueden cancelar pedidos pendientes");
    }


    for (Order.OrderItem item : order.getItems()) {
      try {
        productService.updateStock(item.getProductId(), item.getQuantity());
      } catch (Exception e) {

        System.err.println("Error devolviendo stock del producto " + item.getProductId() + ": " + e.getMessage());
      }
    }

    order.cancel();
    return orderRepository.save(order);
  }

  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public List<Order> getOrdersByUser(Long userId) {
    return orderRepository.findByUserId(userId);
  }
}
