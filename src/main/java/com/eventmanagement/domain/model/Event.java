package com.eventmanagement.domain.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class Event {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status;
    private Venue venue;
    private Set<Category> categories;

    public Event() {}

    public Event(UUID id, String name, String description,
                 LocalDateTime startDate, LocalDateTime endDate,
                 EventStatus status, Venue venue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.venue = venue;
    }

    public boolean isActive() {
        return status == EventStatus.ACTIVE;
    }

    public boolean isOverlapping(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return !(endDate.isBefore(otherStart) || startDate.isAfter(otherEnd));
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public Set<Category> getCategories() { return categories; }
    public void setCategories(Set<Category> categories) { this.categories = categories; }
}

