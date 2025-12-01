package com.example.hexagonalapp.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues", indexes = {
  @Index(name = "idx_venue_active", columnList = "active"),
  @Index(name = "idx_venue_location", columnList = "location"),
  @Index(name = "idx_venue_capacity", columnList = "capacity")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String location;

  @Column(nullable = false)
  private Integer capacity;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false)
  private boolean active;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Relación OneToMany con EventEntity - Lazy loading por defecto
  @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<EventEntity> events = new ArrayList<>();
}
