package com.example.hexagonalapp.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events", indexes = {
  @Index(name = "idx_event_status", columnList = "status"),
  @Index(name = "idx_event_start_date", columnList = "start_date"),
  @Index(name = "idx_event_venue", columnList = "venue_id"),
  @Index(name = "idx_event_active_upcoming", columnList = "status, start_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(length = 2000)
  private String description;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "available_tickets", nullable = false)
  private Integer availableTickets;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EventStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "venue_id", nullable = false, foreignKey = @ForeignKey(name = "fk_event_venue"))
  private VenueEntity venue;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
    name = "event_categories",
    joinColumns = @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "fk_event_category_event")),
    inverseJoinColumns = @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_event_category_category"))
  )
  private Set<CategoryEntity> categories = new HashSet<>();

  public enum EventStatus {
    ACTIVE, CANCELLED, COMPLETED, SOLD_OUT
  }
}
