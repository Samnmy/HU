package com.example.hexagonalapp.infrastructure.repositories;

import com.example.hexagonalapp.domain.models.Order;
import com.example.hexagonalapp.domain.repositories.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;

  public OrderRepositoryAdapter(JpaOrderRepository jpaOrderRepository) {
    this.jpaOrderRepository = jpaOrderRepository;
  }

  private OrderEntity toEntity(Order order) {
    OrderEntity entity = new OrderEntity();
    entity.setId(order.getId());
    entity.setUserId(order.getUserId());
    entity.setTotalAmount(order.getTotalAmount());
    entity.setStatus(OrderEntity.OrderStatus.valueOf(order.getStatus().name()));
    entity.setCreatedAt(order.getCreatedAt());
    entity.setUpdatedAt(order.getUpdatedAt());

    // Convertir items
    List<OrderItemEntity> itemEntities = order.getItems().stream()
      .map(item -> {
        OrderItemEntity itemEntity = new OrderItemEntity();
        itemEntity.setOrder(entity);
        itemEntity.setProductId(item.getProductId());
        itemEntity.setProductName(item.getProductName());
        itemEntity.setUnitPrice(item.getUnitPrice());
        itemEntity.setQuantity(item.getQuantity());
        return itemEntity;
      })
      .collect(Collectors.toList());

    entity.setItems(itemEntities);
    return entity;
  }

  private Order toDomain(OrderEntity entity) {
    Order order = new Order();
    order.setId(entity.getId());
    order.setUserId(entity.getUserId());
    order.setTotalAmount(entity.getTotalAmount());
    order.setStatus(Order.OrderStatus.valueOf(entity.getStatus().name()));
    order.setCreatedAt(entity.getCreatedAt());
    order.setUpdatedAt(entity.getUpdatedAt());

    // Convertir items
    List<Order.OrderItem> items = entity.getItems().stream()
      .map(item -> new Order.OrderItem(
        item.getProductId(),
        item.getProductName(),
        item.getUnitPrice(),
        item.getQuantity()
      ))
      .collect(Collectors.toList());

    order.setItems(items);
    return order;
  }

  @Override
  public Order save(Order order) {
    OrderEntity entity = toEntity(order);
    OrderEntity savedEntity = jpaOrderRepository.save(entity);
    return toDomain(savedEntity);
  }

  @Override
  public Optional<Order> findById(Long id) {
    return jpaOrderRepository.findById(id)
      .map(this::toDomain);
  }

  @Override
  public List<Order> findAll() {
    return jpaOrderRepository.findAll().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Order> findByUserId(Long userId) {
    return jpaOrderRepository.findByUserId(userId).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaOrderRepository.deleteById(id);
  }
}
