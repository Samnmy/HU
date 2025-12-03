package com.example.hexagonalapp.domain.ports.in;

import com.example.hexagonalapp.domain.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventUseCase {
    Event createEvent(Event event);
    Optional<Event> getEventById(Long id);
    List<Event> getAllEvents();
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);
    List<Event> getEventsByVenue(Long venueId);
}