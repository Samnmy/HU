package com.eventmanagement.domain.ports.in;

import com.eventmanagement.application.dto.EventRequest;
import com.eventmanagement.application.dto.EventResponse;

import java.util.List;
import java.util.UUID;

public interface EventUseCase {
    EventResponse createEvent(EventRequest request);
    EventResponse getEventById(UUID id);
    List<EventResponse> getAllEvents(int page, int size, String sortBy, String direction);
    EventResponse updateEvent(UUID id, EventRequest request);
    void deleteEvent(UUID id);
    List<EventResponse> getEventsByVenue(UUID venueId);
    List<EventResponse> getEventsByDateRange(String startDate, String endDate);
    List<EventResponse> filterEvents(String status, String city, String category);
}