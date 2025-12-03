package com.example.hexagonalapp.infrastructure.adapters.in.web;

import com.example.hexagonalapp.application.dto.EventRequest;
import com.example.hexagonalapp.application.dto.EventResponse;
import com.example.hexagonalapp.domain.model.Event;
import com.example.hexagonalapp.domain.ports.in.EventUseCase;
import com.example.hexagonalapp.infrastructure.mapper.EventDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventUseCase eventUseCase;
    private final EventDtoMapper eventDtoMapper;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        Event event = eventDtoMapper.toDomain(request);
        Event created = eventUseCase.createEvent(event);
        EventResponse response = eventDtoMapper.toResponse(created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        List<Event> events = eventUseCase.getAllEvents();
        List<EventResponse> responses = events.stream()
                .map(eventDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        return eventUseCase.getEventById(id)
                .map(event -> ResponseEntity.ok(eventDtoMapper.toResponse(event)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventResponse>> getByVenue(@PathVariable Long venueId) {
        List<Event> events = eventUseCase.getEventsByVenue(venueId);
        List<EventResponse> responses = events.stream()
                .map(eventDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody EventRequest request) {
        Event event = eventDtoMapper.toDomain(request);
        Event updated = eventUseCase.updateEvent(id, event);
        EventResponse response = eventDtoMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}