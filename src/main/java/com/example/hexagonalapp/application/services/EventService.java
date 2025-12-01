package com.example.hexagonalapp.application.services;

import com.example.hexagonalapp.domain.models.Event;
import com.example.hexagonalapp.domain.models.Venue;
import com.example.hexagonalapp.domain.repositories.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

  private final EventRepository eventRepository;
  private final VenueService venueService;
  private final CategoryService categoryService;

  public EventService(EventRepository eventRepository, VenueService venueService, CategoryService categoryService) {
    this.eventRepository = eventRepository;
    this.venueService = venueService;
    this.categoryService = categoryService;
  }

  @Transactional
  public Event createEvent(String title, String description, LocalDateTime startDate,
                           LocalDateTime endDate, BigDecimal price, Integer availableTickets,
                           Long venueId) {

    Venue venue = venueService.getVenueById(venueId);

    Event event = new Event(title, description, startDate, endDate, price, availableTickets, venueId);
    return eventRepository.save(event);
  }

  @Transactional(readOnly = true)
  public List<Event> getAllEvents() {
    return eventRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Event getEventById(Long id) {
    return eventRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
  }

  @Transactional(readOnly = true)
  public List<Event> getEventsByVenue(Long venueId) {
    return eventRepository.findByVenueId(venueId);
  }

  @Transactional(readOnly = true)
  public List<Event> getActiveEvents() {
    return eventRepository.findByStatus(Event.EventStatus.ACTIVE);
  }

  @Transactional(readOnly = true)
  public List<Event> getUpcomingEvents() {
    return eventRepository.findByStartDateAfter(LocalDateTime.now());
  }

  @Transactional(readOnly = true)
  public List<Event> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
    return eventRepository.findByStartDateBetween(start, end);
  }

  @Transactional(readOnly = true)
  public List<Event> searchEventsByTitle(String title) {
    return eventRepository.findByTitleContaining(title);
  }

  @Transactional
  public Event cancelEvent(Long id) {
    Event event = getEventById(id);
    event.cancel();
    return eventRepository.save(event);
  }

  @Transactional
  public Event completeEvent(Long id) {
    Event event = getEventById(id);
    event.complete();
    return eventRepository.save(event);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean bookTickets(Long eventId, Integer quantity) {
    Event event = getEventById(eventId);

    if (event.getStatus() != Event.EventStatus.ACTIVE) {
      throw new RuntimeException("El evento no está activo para reservas");
    }

    boolean success = event.bookTickets(quantity);
    if (success) {
      eventRepository.save(event);
    }
    return success;
  }

  @Transactional
  public void deleteEvent(Long id) {
    eventRepository.deleteById(id);
  }
}
