package com.example.hexagonalapp.domain.ports.out;

import com.example.hexagonalapp.domain.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {
    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    void deleteById(Long id);
    boolean existsByName(String name);
    List<Event> findByVenueId(Long venueId);
}