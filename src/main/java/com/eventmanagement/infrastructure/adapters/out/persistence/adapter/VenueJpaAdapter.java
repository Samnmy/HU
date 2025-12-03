package com.eventmanagement.infrastructure.adapters.out.persistence.adapter;

import com.eventmanagement.domain.model.Venue;
import com.eventmanagement.domain.ports.out.VenueRepositoryPort;
import com.eventmanagement.infrastructure.adapters.out.persistence.mapper.PersistenceVenueMapper;
import com.eventmanagement.infrastructure.adapters.out.persistence.repository.VenueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final VenueJpaRepository venueJpaRepository;
    private final PersistenceVenueMapper persistenceVenueMapper;

    @Override
    public Venue save(Venue venue) {
        var venueEntity = persistenceVenueMapper.toEntity(venue);
        var savedEntity = venueJpaRepository.save(venueEntity);
        return persistenceVenueMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Venue> findById(UUID id) {
        return venueJpaRepository.findById(id)
                .map(persistenceVenueMapper::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return venueJpaRepository.findAll().stream()
                .map(persistenceVenueMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Venue> findByCity(String city) {
        return venueJpaRepository.findByCity(city).stream()
                .map(persistenceVenueMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Venue> findByCapacityRange(Integer minCapacity, Integer maxCapacity) {
        return venueJpaRepository.findByCapacityRange(minCapacity, maxCapacity).stream()
                .map(persistenceVenueMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        venueJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return venueJpaRepository.existsByName(name);
    }

    @Override
    public List<Venue> findAllPaginated(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return venueJpaRepository.findAll(pageable).getContent().stream()
                .map(persistenceVenueMapper::toDomain)
                .collect(Collectors.toList());
    }
}