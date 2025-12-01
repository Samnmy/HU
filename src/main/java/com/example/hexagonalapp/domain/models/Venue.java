package com.example.hexagonalapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
  private Long id;
  private String name;
  private String location;
  private Integer capacity;
  private String description;
  private boolean active;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<Event> events = new ArrayList<>();

  public Venue(String name, String location, Integer capacity, String description) {
    this.name = name;
    this.location = location;
    this.capacity = capacity;
    this.description = description;
    this.active = true;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void addEvent(Event event) {
    if (events == null) {
      events = new ArrayList<>();
    }
    events.add(event);
    this.updatedAt = LocalDateTime.now();
  }

  public void deactivate() {
    this.active = false;
    this.updatedAt = LocalDateTime.now();
  }

  public void activate() {
    this.active = true;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateDetails(String name, String location, Integer capacity, String description) {
    this.name = name;
    this.location = location;
    this.capacity = capacity;
    this.description = description;
    this.updatedAt = LocalDateTime.now();
  }
}
