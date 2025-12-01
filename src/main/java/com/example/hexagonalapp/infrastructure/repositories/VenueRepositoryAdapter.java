package com.example.hexagonalapp.infrastructure.repositories;

import com.example.hexagonalapp.domain.models.Venue;
import com.example.hexagonalapp.domain.repositories.VenueRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VenueRepositoryAdapter implements VenueRepository {

  private final JpaVenueRepository jpaVenueRepository;

  public VenueRepositoryAdapter(JpaVenueRepository jpaVenueRepository) {
    this.jpaVenueRepository = jpaVenueRepository;
  }

  private VenueEntity toEntity(Venue venue) {
    VenueEntity entity = new VenueEntity();
    entity.setId(venue.getId());
    entity.setName(venue.getName());
    entity.setLocation(venue.getLocation());
    entity.setCapacity(venue.getCapacity());
    entity.setDescription(venue.getDescription());
    entity.setActive(venue.isActive());
    entity.setCreatedAt(venue.getCreatedAt());
    entity.setUpdatedAt(venue.getUpdatedAt());
    return entity;
  }

  private Venue toDomain(VenueEntity entity) {
    Venue venue = new Venue(
      entity.getId(),
      entity.getName(),
      entity.getLocation(),
      entity.getCapacity(),
      entity.getDescription(),
      entity.isActive(),
      entity.getCreatedAt(),
      entity.getUpdatedAt(),
      null
    );
    return venue;
  }

  @Override
  public Venue save(Venue venue) {
    VenueEntity entity = toEntity(venue);
    VenueEntity savedEntity = jpaVenueRepository.save(entity);
    return toDomain(savedEntity);
  }

  @Override
  public Optional<Venue> findById(Long id) {
    return jpaVenueRepository.findById(id)
      .map(this::toDomain);
  }

  @Override
  public List<Venue> findAll() {
    return jpaVenueRepository.findAll().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Venue> findByActiveTrue() {
    return jpaVenueRepository.findByActiveTrue().stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Venue> findByLocationContaining(String location) {
    return jpaVenueRepository.findByLocationContaining(location).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public List<Venue> findByCapacityGreaterThanEqual(Integer minCapacity) {
    return jpaVenueRepository.findByCapacityGreaterThanEqual(minCapacity).stream()
      .map(this::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaVenueRepository.deleteById(id);
  }
}
