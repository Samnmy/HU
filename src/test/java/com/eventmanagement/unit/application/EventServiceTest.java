package com.eventmanagement.unit.application;

import com.eventmanagement.application.dto.EventRequest;
import com.eventmanagement.application.mapper.EventMapper;
import com.eventmanagement.application.usecase.EventService;
import com.eventmanagement.domain.model.Event;
import com.eventmanagement.domain.model.Venue;
import com.eventmanagement.domain.ports.out.EventRepositoryPort;
import com.eventmanagement.domain.ports.out.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private VenueRepositoryPort venueRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private UUID venueId;
    private UUID eventId;
    private Venue venue;
    private Event event;
    private EventRequest eventRequest;

    @BeforeEach
    void setUp() {
        venueId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        venue = new Venue(venueId, "Test Venue", "Test Location", "Test City", 1000);

        event = new Event();
        event.setId(eventId);
        event.setName("Test Event");
        event.setDescription("Test Description");
        event.setStartDate(LocalDateTime.now().plusDays(1));
        event.setEndDate(LocalDateTime.now().plusDays(2));
        event.setVenue(venue);

        eventRequest = new EventRequest(
                "Test Event",
                "Test Description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                venueId,
                new String[]{"Music", "Concert"}
        );
    }

    @Test
    void createEvent_Success() {
        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));
        when(eventRepository.existsByName(any())).thenReturn(false);
        when(eventMapper.toDomain(any())).thenReturn(event);
        when(eventRepository.save(any())).thenReturn(event);

        var result = eventService.createEvent(eventRequest);

        assertNotNull(result);
        verify(venueRepository).findById(venueId);
        verify(eventRepository).existsByName(any());
        verify(eventRepository).save(any());
    }

    @Test
    void getEventById_Success() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        var result = eventService.getEventById(eventId);

        assertNotNull(result);
        verify(eventRepository).findById(eventId);
    }
}