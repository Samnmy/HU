package com.eventmanagement.domain.ports.in;

import com.eventmanagement.application.dto.VenueRequest;
import com.eventmanagement.application.dto.VenueResponse;

import java.util.List;
import java.util.UUID;

public interface VenueUseCase {
    VenueResponse createVenue(VenueRequest request);
    VenueResponse getVenueById(UUID id);
    List<VenueResponse> getAllVenues(int page, int size, String sortBy, String direction);
    VenueResponse updateVenue(UUID id, VenueRequest request);
    void deleteVenue(UUID id);
    List<VenueResponse> getVenuesByCity(String city);
    List<VenueResponse> getVenuesByCapacity(Integer minCapacity, Integer maxCapacity);
}