package com.eventmanagement.domain.ports.out;

import com.eventmanagement.domain.model.Venue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VenueRepositoryPort {
    Venue save(Venue venue);
    Optional<Venue> findById(UUID id);
    List<Venue> findAll();
    List<Venue> findByCity(String city);
    List<Venue> findByCapacityRange(Integer minCapacity, Integer maxCapacity);
    void deleteById(UUID id);
    boolean existsByName(String name);
    List<Venue> findAllPaginated(int page, int size, String sortBy, String direction);
}