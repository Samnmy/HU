package com.example.hexagonalapp.infrastructure.repositories;

import com.example.hexagonalapp.domain.models.Event;
import com.example.hexagonalapp.domain.repositories.EventRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EventRepositoryAdapter implements EventRepository {

  private final JpaEventRepository jpaEventRepository;
  private final JpaVenueRepository jpaVenueRepository;

  public EventRepositoryAdapter(JpaEventRepository jpaEventRepository, JpaVenueRepository jpaVenueRepository) {
    this.jpaEventRepository = jpaEventRepository;
    this.jpaVenueRepository = jpaVenueRepository;
  }

  private EventEntity toEntity(Event event) {
    EventEntity entity = new EventEntity();
    entity.setId(event.getId());
    entity.setTitle(event.getTitle());
    entity.setDescription(event.getDescription());
    entity.setStartDate(event.getStartDate());
    entity.setEndDate(event.getEndDate());
    entity.setPrice(event.getPrice());
    entity.setAvailableTickets(event.getAvailableTickets());
    entity.setStatus(EventEntity.EventStatus.valueOf(event.getStatus().name()));
    entity.setCreatedAt(event.getCreatedAt());
    entity.setUpdatedAt(event.getUpdatedAt());

    if (event.getVenueId() != null) {
      jpaVenueRepository.findById(event.getVenueId()).ifPresent(entity::setVenue);
    }

    return entity;
  }

  private Event toDomain(EventEntity entity) {
    Event event = new Event(
      entity.getId(),
      entity.getTitle(),
      entity.getDescription(),
      entity.getStartDate(),
      entity.getEndDate(),
      entity.getPrice(),
      entity.getAvailableTickets(),
      Event.EventStatus.valueOf(entity.getStatus().name()),
      entity.getCreatedAt(),
      entity.getUpdatedAt(),
      entity.getVenue() != null ? entity.getVenue().getId() : null,
      null,
      null
    );
    return event;
  }

  @Override
  public Event save(Event event) {
    EventEntity entity = toEntity(event);
    EventEntity savedEntity = jpaEventRepository.save(entity);
    return toDomain(savedEntity);
  }

  @Override
  public Optional<Event> findById(Long id) {
    return Optional.ofNullable(jpaEventRepository.findByIdWithVenue(id))
      .map(this::toDomain);
  }

  @Override
  public List<Event> findAll() {
    return jpaEventRepository.findAll().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Event> findByVenueId(Long venueId) {
    return jpaEventRepository.findByVenueId(venueId).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Event> findByStatus(Event.EventStatus status) {
    return jpaEventRepository.findByStatus(EventEntity.EventStatus.valueOf(status.name())).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Event> findByStartDateBetween(LocalDateTime start, LocalDateTime end) {
    return jpaEventRepository.findByStartDateBetween(start, end).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Event> findByStartDateAfter(LocalDateTime date) {
    return jpaEventRepository.findByStartDateAfter(date).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Event> findByTitleContaining(String title) {
    return jpaEventRepository.findByTitleContaining(title).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Event> findByVenueIdAndStatus(Long venueId, Event.EventStatus status) {
    return jpaEventRepository.findByVenueIdAndStatus(
        venueId,
        EventEntity.EventStatus.valueOf(status.name())
      ).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaEventRepository.deleteById(id);
  }
}
