package com.example.hexagonalapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  private Long id;
  private String title;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private BigDecimal price;
  private Integer availableTickets;
  private EventStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Relación ManyToOne con Venue
  private Long venueId;
  private Venue venue;

  // Relación ManyToMany con Category (opcional)
  private Set<Category> categories = new HashSet<>();

  public Event(String title, String description, LocalDateTime startDate,
               LocalDateTime endDate, BigDecimal price, Integer availableTickets, Long venueId) {
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
    this.availableTickets = availableTickets;
    this.venueId = venueId;
    this.status = EventStatus.ACTIVE;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public enum EventStatus {
    ACTIVE, CANCELLED, COMPLETED, SOLD_OUT
  }

  // Métodos de negocio
  public void cancel() {
    if (this.status != EventStatus.CANCELLED) {
      this.status = EventStatus.CANCELLED;
      this.updatedAt = LocalDateTime.now();
    }
  }

  public void complete() {
    this.status = EventStatus.COMPLETED;
    this.updatedAt = LocalDateTime.now();
  }

  public void markAsSoldOut() {
    this.status = EventStatus.SOLD_OUT;
    this.updatedAt = LocalDateTime.now();
  }

  public boolean bookTickets(Integer quantity) {
    if (quantity <= 0 || quantity > availableTickets) {
      return false;
    }
    this.availableTickets -= quantity;

    if (this.availableTickets == 0) {
      markAsSoldOut();
    }

    this.updatedAt = LocalDateTime.now();
    return true;
  }

  public void addCategory(Category category) {
    this.categories.add(category);
  }

  public void removeCategory(Category category) {
    this.categories.remove(category);
  }
}
