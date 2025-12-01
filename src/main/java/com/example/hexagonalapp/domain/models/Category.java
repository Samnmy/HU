package com.example.hexagonalapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
  private Long id;
  private String name;
  private String description;
  private boolean active;
  private LocalDateTime createdAt;
  private Set<Event> events = new HashSet<>();

  public Category(String name, String description) {
    this.name = name;
    this.description = description;
    this.active = true;
    this.createdAt = LocalDateTime.now();
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }
}
