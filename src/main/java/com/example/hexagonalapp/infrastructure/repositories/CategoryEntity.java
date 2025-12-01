package com.example.hexagonalapp.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories", indexes = {
  @Index(name = "idx_category_active", columnList = "active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(nullable = false)
  private boolean active;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
  private Set<EventEntity> events = new HashSet<>();
}
