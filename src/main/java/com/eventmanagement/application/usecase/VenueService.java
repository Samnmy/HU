package com.eventmanagement.application.usecase;

import com.eventmanagement.domain.model.Venue;
import com.eventmanagement.domain.ports.in.VenueUseCase;
import com.eventmanagement.domain.ports.out.VenueRepositoryPort;
import com.eventmanagement.application.dto.VenueRequest;
import com.eventmanagement.application.dto.VenueResponse;
import com.eventmanagement.application.mapper.VenueMapper;
import com.eventmanagement.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenueService implements VenueUseCase {

    private final VenueRepositoryPort venueRepository;
    private final VenueMapper venueMapper;

    @Override
    @Transactional
    public VenueResponse createVenue(VenueRequest request) {
        log.info("Creating venue with name: {}", request.getName());

        if (venueRepository.existsByName(request.getName())) {
            throw new DomainException("Venue with name '" + request.getName() + "' already exists");
        }

        Venue venue = venueMapper.toDomain(request);
        venue.setId(UUID.randomUUID());

        Venue savedVenue = venueRepository.save(venue);
        log.info("Venue created successfully with id: {}", savedVenue.getId());

        return venueMapper.toResponse(savedVenue);
    }

    @Override
    @Transactional(readOnly = true)
    public VenueResponse getVenueById(UUID id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new DomainException("Venue not found with id: " + id));
        return venueMapper.toResponse(venue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponse> getAllVenues(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Venue> venues = venueRepository.findAllPaginated(page, size, sortBy, direction);
        return venues.stream()
                .map(venueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VenueResponse updateVenue(UUID id, VenueRequest request) {
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new DomainException("Venue not found with id: " + id));

        if (!existingVenue.getName().equals(request.getName()) &&
                venueRepository.existsByName(request.getName())) {
            throw new DomainException("Venue with name '" + request.getName() + "' already exists");
        }

        existingVenue.setName(request.getName());
        existingVenue.setLocation(request.getLocation());
        existingVenue.setCity(request.getCity());
        existingVenue.setCapacity(request.getCapacity());

        Venue updatedVenue = venueRepository.save(existingVenue);
        log.info("Venue updated successfully with id: {}", id);

        return venueMapper.toResponse(updatedVenue);
    }

    @Override
    @Transactional
    public void deleteVenue(UUID id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new DomainException("Venue not found with id: " + id));

        if (venue.hasEvents()) {
            throw new DomainException("Cannot delete venue with associated events");
        }

        venueRepository.deleteById(id);
        log.info("Venue deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponse> getVenuesByCity(String city) {
        List<Venue> venues = venueRepository.findByCity(city);
        return venues.stream()
                .map(venueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponse> getVenuesByCapacity(Integer minCapacity, Integer maxCapacity) {
        List<Venue> venues = venueRepository.findByCapacityRange(minCapacity, maxCapacity);
        return venues.stream()
                .map(venueMapper::toResponse)
                .collect(Collectors.toList());
    }
}