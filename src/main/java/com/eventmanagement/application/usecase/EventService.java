package com.eventmanagement.application.usecase;

import com.eventmanagement.domain.model.Event;
import com.eventmanagement.domain.model.EventStatus;
import com.eventmanagement.domain.model.Venue;
import com.eventmanagement.domain.ports.in.EventUseCase;
import com.eventmanagement.domain.ports.out.EventRepositoryPort;
import com.eventmanagement.domain.ports.out.VenueRepositoryPort;
import com.eventmanagement.application.dto.EventRequest;
import com.eventmanagement.application.dto.EventResponse;
import com.eventmanagement.application.mapper.EventMapper;
import com.eventmanagement.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService implements EventUseCase {

    private final EventRepositoryPort eventRepository;
    private final VenueRepositoryPort venueRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        log.info("Creating event with name: {}", request.getName());

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new DomainException("Venue not found with id: " + request.getVenueId()));

        if (eventRepository.existsByName(request.getName())) {
            throw new DomainException("Event with name '" + request.getName() + "' already exists");
        }

        validateDateRange(request.getStartDate(), request.getEndDate());

        List<Event> overlappingEvents = eventRepository.findByDateRange(
                        request.getStartDate(), request.getEndDate()
                ).stream()
                .filter(e -> e.getVenue().getId().equals(request.getVenueId()))
                .collect(Collectors.toList());

        if (!overlappingEvents.isEmpty()) {
            throw new DomainException("Event overlaps with existing events at the same venue");
        }

        Event event = eventMapper.toDomain(request);
        event.setVenue(venue);
        event.setStatus(EventStatus.ACTIVE);
        event.setId(UUID.randomUUID());

        Event savedEvent = eventRepository.save(event);
        log.info("Event created successfully with id: {}", savedEvent.getId());

        return eventMapper.toResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new DomainException("Event not found with id: " + id));
        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Event> events = eventRepository.findAllPaginated(page, size, sortBy, direction);
        return events.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponse updateEvent(UUID id, EventRequest request) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new DomainException("Event not found with id: " + id));

        existingEvent.setName(request.getName());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setStartDate(request.getStartDate());
        existingEvent.setEndDate(request.getEndDate());

        if (!existingEvent.getVenue().getId().equals(request.getVenueId())) {
            Venue newVenue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new DomainException("Venue not found with id: " + request.getVenueId()));
            existingEvent.setVenue(newVenue);
        }

        Event updatedEvent = eventRepository.save(existingEvent);
        log.info("Event updated successfully with id: {}", id);

        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id) {
        if (!eventRepository.findById(id).isPresent()) {
            throw new DomainException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
        log.info("Event deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByVenue(UUID venueId) {
        List<Event> events = eventRepository.findByVenueId(venueId);
        return events.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByDateRange(String startDate, String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        validateDateRange(start, end);

        List<Event> events = eventRepository.findByDateRange(start, end);
        return events.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> filterEvents(String status, String city, String category) {
        List<Event> filteredEvents;

        if (status != null) {
            EventStatus eventStatus = EventStatus.valueOf(status.toUpperCase());
            filteredEvents = eventRepository.findByStatus(eventStatus);
        } else if (city != null) {
            filteredEvents = eventRepository.findByCity(city);
        } else {
            filteredEvents = eventRepository.findAll();
        }

        if (category != null) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> event.getCategories().stream()
                            .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                    .collect(Collectors.toList());
        }

        return filteredEvents.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new DomainException("Start date must be before end date");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new DomainException("Start date must be in the future");
        }
    }
}