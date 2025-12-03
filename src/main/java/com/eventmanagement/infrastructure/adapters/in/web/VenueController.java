package com.eventmanagement.infrastructure.adapters.in.web;

import com.eventmanagement.application.dto.VenueRequest;
import com.eventmanagement.application.dto.VenueResponse;
import com.eventmanagement.domain.ports.in.VenueUseCase;
import com.eventmanagement.infrastructure.adapters.in.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
@Tag(name = "Venues", description = "Venue Management API")
public class VenueController {

    private final VenueUseCase venueUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new venue")
    public ResponseEntity<ApiResponse<VenueResponse>> createVenue(
            @Valid @RequestBody VenueRequest request) {
        VenueResponse response = venueUseCase.createVenue(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Venue created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get venue by ID")
    public ResponseEntity<ApiResponse<VenueResponse>> getVenue(@PathVariable UUID id) {
        VenueResponse response = venueUseCase.getVenueById(id);
        return ResponseEntity.ok(ApiResponse.success("Venue retrieved successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all venues with pagination")
    public ResponseEntity<ApiResponse<List<VenueResponse>>> getAllVenues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        List<VenueResponse> venues = venueUseCase.getAllVenues(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success("Venues retrieved successfully", venues));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a venue")
    public ResponseEntity<ApiResponse<VenueResponse>> updateVenue(
            @PathVariable UUID id,
            @Valid @RequestBody VenueRequest request) {
        VenueResponse response = venueUseCase.updateVenue(id, request);
        return ResponseEntity.ok(ApiResponse.success("Venue updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a venue")
    public ResponseEntity<ApiResponse<Void>> deleteVenue(@PathVariable UUID id) {
        venueUseCase.deleteVenue(id);
        return ResponseEntity.ok(ApiResponse.success("Venue deleted successfully", null));
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get venues by city")
    public ResponseEntity<ApiResponse<List<VenueResponse>>> getVenuesByCity(
            @PathVariable String city) {
        List<VenueResponse> venues = venueUseCase.getVenuesByCity(city);
        return ResponseEntity.ok(ApiResponse.success("Venues retrieved successfully", venues));
    }

    @GetMapping("/capacity")
    @Operation(summary = "Get venues by capacity range")
    public ResponseEntity<ApiResponse<List<VenueResponse>>> getVenuesByCapacity(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer maxCapacity) {
        List<VenueResponse> venues = venueUseCase.getVenuesByCapacity(minCapacity, maxCapacity);
        return ResponseEntity.ok(ApiResponse.success("Venues retrieved successfully", venues));
    }
}