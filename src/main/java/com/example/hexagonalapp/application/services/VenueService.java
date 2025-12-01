package com.example.hexagonalapp.application.services;

import com.example.hexagonalapp.domain.models.Venue;
import com.example.hexagonalapp.domain.repositories.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueService {

  private final VenueRepository venueRepository;

  public VenueService(VenueRepository venueRepository) {
    this.venueRepository = venueRepository;
  }

  @Transactional
  public Venue createVenue(String name, String location, Integer capacity, String description) {
    Venue venue = new Venue(name, location, capacity, description);
    return venueRepository.save(venue);
  }

  @Transactional(readOnly = true)
  public List<Venue> getAllVenues() {
    return venueRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Venue> getActiveVenues() {
    return venueRepository.findByActiveTrue();
  }

  @Transactional(readOnly = true)
  public Venue getVenueById(Long id) {
    return venueRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + id));
  }

  @Transactional
  public Venue updateVenue(Long id, String name, String location, Integer capacity, String description) {
    Venue venue = getVenueById(id);
    venue.updateDetails(name, location, capacity, description);
    return venueRepository.save(venue);
  }

  @Transactional
  public void deactivateVenue(Long id) {
    Venue venue = getVenueById(id);
    venue.deactivate();
    venueRepository.save(venue);
  }

  @Transactional
  public void activateVenue(Long id) {
    Venue venue = getVenueById(id);
    venue.activate();
    venueRepository.save(venue);
  }

  @Transactional
  public void deleteVenue(Long id) {
    venueRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<Venue> searchVenuesByLocation(String location) {
    return venueRepository.findByLocationContaining(location);
  }

  @Transactional(readOnly = true)
  public List<Venue> searchVenuesByMinCapacity(Integer minCapacity) {
    return venueRepository.findByCapacityGreaterThanEqual(minCapacity);
  }
}
