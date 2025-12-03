package com.eventmanagement.infrastructure.adapters.out.persistence.adapter;

import com.eventmanagement.domain.model.Event;
import com.eventmanagement.domain.model.EventStatus;
import com.eventmanagement.domain.ports.out.EventRepositoryPort;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.EventEntity;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.VenueEntity;
import com.eventmanagement.infrastructure.adapters.out.persistence.mapper.PersistenceEventMapper;
import com.eventmanagement.infrastructure.adapters.out.persistence.repository.EventJpaRepository;
import com.eventmanagement.infrastructure.adapters.out.persistence.repository.VenueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventJpaRepository eventJpaRepository;
    private final VenueJpaRepository venueJpaRepository;
    private final PersistenceEventMapper persistenceEventMapper;

    @Override
    public Event save(Event event) {
        EventEntity eventEntity = persistenceEventMapper.toEntity(event);

        VenueEntity venueEntity = venueJpaRepository.findById(event.getVenue().getId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        eventEntity.setVenue(venueEntity);

        EventEntity savedEntity = eventJpaRepository.save(eventEntity);
        return persistenceEventMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return eventJpaRepository.findById(id)
                .map(persistenceEventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventJpaRepository.findAll().stream()
                .map(persistenceEventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByVenueId(UUID venueId) {
        return eventJpaRepository.findByVenueId(venueId).stream()
                .map(persistenceEventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByStatus(EventStatus status) {
        return eventJpaRepository.findByStatus(status.name()).stream()
                .map(persistenceEventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventJpaRepository.findByDateRange(start, end).stream()
                .map(persistenceEventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByCity(String city) {
        return eventJpaRepository.findByCity(city).stream()
                .map(persistenceEventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        eventJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return eventJpaRepository.existsByName(name);
    }

    @Override
    public List<Event> findAllPaginated(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return eventJpaRepository.findAll(pageable).getContent().stream()
                .map(persistenceEventMapper::toDomain)
                .collect(Collectors.toList());
    }
}