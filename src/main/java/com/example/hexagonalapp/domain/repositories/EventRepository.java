package com.example.hexagonalapp.domain.repositories;

import com.example.hexagonalapp.domain.models.Event;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
  Event save(Event event);
  Optional<Event> findById(Long id);
  List<Event> findAll();
  List<Event> findByVenueId(Long venueId);
  List<Event> findByStatus(Event.EventStatus status);
  List<Event> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
  List<Event> findByStartDateAfter(LocalDateTime date);
  List<Event> findByTitleContaining(String title);
  List<Event> findByVenueIdAndStatus(Long venueId, Event.EventStatus status);
  void deleteById(Long id);
}
