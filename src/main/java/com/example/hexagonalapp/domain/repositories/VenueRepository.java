package com.example.hexagonalapp.domain.repositories;

import com.example.hexagonalapp.domain.models.Venue;
import java.util.List;
import java.util.Optional;

public interface VenueRepository {
  Venue save(Venue venue);
  Optional<Venue> findById(Long id);
  List<Venue> findAll();
  List<Venue> findByActiveTrue();
  List<Venue> findByLocationContaining(String location);
  List<Venue> findByCapacityGreaterThanEqual(Integer minCapacity);
  void deleteById(Long id);
}
