package com.example.hexagonalapp.infrastructure.controllers;

import com.example.hexagonalapp.application.services.EventService;
import com.example.hexagonalapp.domain.models.Event;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping
  public ResponseEntity<Event> createEvent(@RequestBody CreateEventRequest request) {
    Event event = eventService.createEvent(
      request.getTitle(),
      request.getDescription(),
      request.getStartDate(),
      request.getEndDate(),
      request.getPrice(),
      request.getAvailableTickets(),
      request.getVenueId()
    );
    return ResponseEntity.created(URI.create("/api/events/" + event.getId())).body(event);
  }

  @GetMapping
  public ResponseEntity<List<Event>> getAllEvents() {
    return ResponseEntity.ok(eventService.getAllEvents());
  }

  @GetMapping("/active")
  public ResponseEntity<List<Event>> getActiveEvents() {
    return ResponseEntity.ok(eventService.getActiveEvents());
  }

  @GetMapping("/upcoming")
  public ResponseEntity<List<Event>> getUpcomingEvents() {
    return ResponseEntity.ok(eventService.getUpcomingEvents());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Event> getEventById(@PathVariable Long id) {
    return ResponseEntity.ok(eventService.getEventById(id));
  }

  @GetMapping("/venue/{venueId}")
  public ResponseEntity<List<Event>> getEventsByVenue(@PathVariable Long venueId) {
    return ResponseEntity.ok(eventService.getEventsByVenue(venueId));
  }

  @GetMapping("/search/title")
  public ResponseEntity<List<Event>> searchEventsByTitle(@RequestParam String title) {
    return ResponseEntity.ok(eventService.searchEventsByTitle(title));
  }

  @GetMapping("/search/date-range")
  public ResponseEntity<List<Event>> searchEventsByDateRange(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
    return ResponseEntity.ok(eventService.getEventsByDateRange(start, end));
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<Event> cancelEvent(@PathVariable Long id) {
    Event event = eventService.cancelEvent(id);
    return ResponseEntity.ok(event);
  }

  @PutMapping("/{id}/complete")
  public ResponseEntity<Event> completeEvent(@PathVariable Long id) {
    Event event = eventService.completeEvent(id);
    return ResponseEntity.ok(event);
  }

  @PutMapping("/{id}/book")
  public ResponseEntity<Boolean> bookTickets(@PathVariable Long id, @RequestBody BookTicketsRequest request) {
    boolean success = eventService.bookTickets(id, request.getQuantity());
    return ResponseEntity.ok(success);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
    eventService.deleteEvent(id);
    return ResponseEntity.noContent().build();
  }

  public static class CreateEventRequest {
    private String title;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    private BigDecimal price;
    private Integer availableTickets;
    private Long venueId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getAvailableTickets() { return availableTickets; }
    public void setAvailableTickets(Integer availableTickets) { this.availableTickets = availableTickets; }
    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
  }

  public static class BookTicketsRequest {
    private Integer quantity;

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
  }
}
