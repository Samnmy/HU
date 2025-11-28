package com.example.hexagonalapp.infrastructure.controllers;

import com.example.hexagonalapp.application.services.ProductService;
import com.example.hexagonalapp.application.services.UserService;
import com.example.hexagonalapp.domain.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

  private final UserService userService;
  private final ProductService productService;
  private final OrderRepository orderRepository;

  public DebugController(UserService userService, ProductService productService, OrderRepository orderRepository) {
    this.userService = userService;
    this.productService = productService;
    this.orderRepository = orderRepository;
  }

  @GetMapping("/data")
  public ResponseEntity<Map<String, Object>> getDebugData() {
    Map<String, Object> debugInfo = new HashMap<>();

    debugInfo.put("users", userService.getAllUsers());
    debugInfo.put("products", productService.getAllProducts());
    debugInfo.put("orders", orderRepository.findAll());

    return ResponseEntity.ok(debugInfo);
  }
}
