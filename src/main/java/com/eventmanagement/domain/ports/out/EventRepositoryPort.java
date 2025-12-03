package com.eventmanagement.domain.ports.out;

import com.eventmanagement.domain.model.Event;
import com.eventmanagement.domain.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepositoryPort {
    Event save(Event event);
    Optional<Event> findById(UUID id);
    List<Event> findAll();
    List<Event> findByVenueId(UUID venueId);
    List<Event> findByStatus(EventStatus status);
    List<Event> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<Event> findByCity(String city);
    void deleteById(UUID id);
    boolean existsByName(String name);
    List<Event> findAllPaginated(int page, int size, String sortBy, String direction);
}