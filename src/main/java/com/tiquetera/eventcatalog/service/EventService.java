package com.tiquetera.eventcatalog.service;

import com.tiquetera.eventcatalog.dto.EventDTO;
import com.tiquetera.eventcatalog.entity.EventEntity;
import com.tiquetera.eventcatalog.entity.VenueEntity;
import com.tiquetera.eventcatalog.repository.EventRepository;
import com.tiquetera.eventcatalog.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    // Convertir Entity a DTO
    private EventDTO convertToDTO(EventEntity entity) {
        return new EventDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getEventDate(),
                entity.getTicketPrice(),
                entity.getVenue().getId(),
                entity.getVenue().getName()
        );
    }

    // Convertir DTO a Entity
    private EventEntity convertToEntity(EventDTO dto) {
        VenueEntity venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con id: " + dto.getVenueId()));

        return new EventEntity(
                dto.getName(),
                dto.getDescription(),
                dto.getCategory(),
                dto.getEventDate(),
                dto.getTicketPrice(),
                venue
        );
    }

    // CRUD operations
    public Page<EventDTO> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Optional<EventDTO> findById(Long id) {
        return eventRepository.findById(id)
                .map(this::convertToDTO);
    }

    public EventDTO save(EventDTO eventDTO) {
        // Validar nombre único
        if (eventRepository.existsByName(eventDTO.getName())) {
            throw new RuntimeException("Ya existe un evento con el nombre: " + eventDTO.getName());
        }

        EventEntity entity = convertToEntity(eventDTO);
        EventEntity savedEntity = eventRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    public Optional<EventDTO> update(Long id, EventDTO eventDTO) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // Validar nombre único si cambió
                    if (!existingEvent.getName().equals(eventDTO.getName()) &&
                            eventRepository.existsByName(eventDTO.getName())) {
                        throw new RuntimeException("Ya existe un evento con el nombre: " + eventDTO.getName());
                    }

                    VenueEntity venue = venueRepository.findById(eventDTO.getVenueId())
                            .orElseThrow(() -> new RuntimeException("Venue no encontrado con id: " + eventDTO.getVenueId()));

                    existingEvent.setName(eventDTO.getName());
                    existingEvent.setDescription(eventDTO.getDescription());
                    existingEvent.setCategory(eventDTO.getCategory());
                    existingEvent.setEventDate(eventDTO.getEventDate());
                    existingEvent.setTicketPrice(eventDTO.getTicketPrice());
                    existingEvent.setVenue(venue);

                    EventEntity updatedEntity = eventRepository.save(existingEvent);
                    return convertToDTO(updatedEntity);
                });
    }

    public boolean deleteById(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Métodos de filtrado con paginación
    public Page<EventDTO> findByCity(String city, Pageable pageable) {
        return eventRepository.findByVenueCity(city, pageable)
                .map(this::convertToDTO);
    }

    public Page<EventDTO> findByCategory(String category, Pageable pageable) {
        return eventRepository.findByCategory(category, pageable)
                .map(this::convertToDTO);
    }

    public Page<EventDTO> findByStartDate(LocalDateTime startDate, Pageable pageable) {
        return eventRepository.findByEventDateAfter(startDate, pageable)
                .map(this::convertToDTO);
    }

    public Page<EventDTO> findByFilters(String city, String category, LocalDateTime startDate, Pageable pageable) {
        return eventRepository.findByFilters(city, category, startDate, pageable)
                .map(this::convertToDTO);
    }
}