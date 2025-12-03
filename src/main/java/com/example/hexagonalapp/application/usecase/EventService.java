package com.example.hexagonalapp.application.usecase;

import com.example.hexagonalapp.domain.model.Event;
import com.example.hexagonalapp.domain.ports.in.EventUseCase;
import com.example.hexagonalapp.domain.ports.out.EventRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService implements EventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    @Override
    public Event createEvent(Event event) {
        if (!event.isValid()) {
            throw new IllegalArgumentException("Evento no válido");
        }
        return eventRepositoryPort.save(event);
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepositoryPort.findById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        event.setId(id);
        return eventRepositoryPort.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepositoryPort.deleteById(id);
    }

    @Override
    public List<Event> getEventsByVenue(Long venueId) {
        return eventRepositoryPort.findByVenueId(venueId);
    }
}