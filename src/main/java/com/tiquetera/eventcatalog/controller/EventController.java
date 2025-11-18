package com.tiquetera.eventcatalog.controller;

import com.tiquetera.eventcatalog.dto.EventDTO;
import com.tiquetera.eventcatalog.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // GET con paginación y filtros
    @GetMapping
    public ResponseEntity<Page<EventDTO>> getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<EventDTO> events;
        if (city != null || category != null || startDate != null) {
            events = eventService.findByFilters(city, category, startDate, pageable);
        } else {
            events = eventService.findAll(pageable);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        Optional<EventDTO> event = eventService.findById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        try {
            EventDTO savedEvent = eventService.save(eventDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        try {
            Optional<EventDTO> updatedEvent = eventService.update(id, eventDTO);
            return updatedEvent.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoints específicos para filtros individuales
    @GetMapping("/city/{city}")
    public ResponseEntity<Page<EventDTO>> getEventsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EventDTO> events = eventService.findByCity(city, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<EventDTO>> getEventsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EventDTO> events = eventService.findByCategory(category, pageable);
        return ResponseEntity.ok(events);
    }
}