package com.example.hexagonalapp.infrastructure.adapters.out.persistence;

import com.example.hexagonalapp.domain.model.Event;
import com.example.hexagonalapp.domain.ports.out.EventRepositoryPort;
import com.example.hexagonalapp.infrastructure.entity.EventEntity;
import com.example.hexagonalapp.infrastructure.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public Event save(Event event) {
        EventEntity entity = eventMapper.toEntity(event);
        EventEntity saved = eventRepository.save(entity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return eventRepository.existsByName(name);
    }

    @Override
    public List<Event> findByVenueId(Long venueId) {
        return eventRepository.findByVenueId(venueId).stream()
                .map(eventMapper::toDomain)
                .collect(Collectors.toList());
    }
}