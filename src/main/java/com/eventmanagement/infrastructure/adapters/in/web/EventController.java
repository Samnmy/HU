package com.eventmanagement.infrastructure.adapters.in.web;

import com.eventmanagement.application.dto.EventRequest;
import com.eventmanagement.application.dto.EventResponse;
import com.eventmanagement.domain.ports.in.EventUseCase;
import com.eventmanagement.infrastructure.adapters.in.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Event Management API")
public class EventController {

    private final EventUseCase eventUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @Operation(summary = "Create a new event")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid @RequestBody EventRequest request) {
        EventResponse response = eventUseCase.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Event created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(
            @PathVariable UUID id) {
        EventResponse response = eventUseCase.getEventById(id);
        return ResponseEntity.ok(ApiResponse.success("Event retrieved successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all events with pagination")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        List<EventResponse> events = eventUseCase.getAllEvents(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", events));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @Operation(summary = "Update an event")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody EventRequest request) {
        EventResponse response = eventUseCase.updateEvent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Event updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an event")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable UUID id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted successfully", null));
    }

    @GetMapping("/venue/{venueId}")
    @Operation(summary = "Get events by venue")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByVenue(
            @PathVariable UUID venueId) {
        List<EventResponse> events = eventUseCase.getEventsByVenue(venueId);
        return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", events));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get events by date range")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<EventResponse> events = eventUseCase.getEventsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", events));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter events")
    public ResponseEntity<ApiResponse<List<EventResponse>>> filterEvents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category) {
        List<EventResponse> events = eventUseCase.filterEvents(status, city, category);
        return ResponseEntity.ok(ApiResponse.success("Events filtered successfully", events));
    }
}