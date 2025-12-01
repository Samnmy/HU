package com.example.hexagonalapp.infrastructure.controllers;

import com.example.hexagonalapp.application.services.VenueService;
import com.example.hexagonalapp.domain.models.Venue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/venues")
@CrossOrigin(origins = "*")
public class VenueController {

  private final VenueService venueService;

  public VenueController(VenueService venueService) {
    this.venueService = venueService;
  }

  @PostMapping
  public ResponseEntity<Venue> createVenue(@RequestBody CreateVenueRequest request) {
    Venue venue = venueService.createVenue(
      request.getName(),
      request.getLocation(),
      request.getCapacity(),
      request.getDescription()
    );
    return ResponseEntity.created(URI.create("/api/venues/" + venue.getId())).body(venue);
  }

  @GetMapping
  public ResponseEntity<List<Venue>> getAllVenues() {
    return ResponseEntity.ok(venueService.getAllVenues());
  }

  @GetMapping("/active")
  public ResponseEntity<List<Venue>> getActiveVenues() {
    return ResponseEntity.ok(venueService.getActiveVenues());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
    return ResponseEntity.ok(venueService.getVenueById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody UpdateVenueRequest request) {
    Venue venue = venueService.updateVenue(
      id,
      request.getName(),
      request.getLocation(),
      request.getCapacity(),
      request.getDescription()
    );
    return ResponseEntity.ok(venue);
  }

  @PutMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivateVenue(@PathVariable Long id) {
    venueService.deactivateVenue(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/activate")
  public ResponseEntity<Void> activateVenue(@PathVariable Long id) {
    venueService.activateVenue(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
    venueService.deleteVenue(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search/location")
  public ResponseEntity<List<Venue>> searchByLocation(@RequestParam String location) {
    return ResponseEntity.ok(venueService.searchVenuesByLocation(location));
  }

  @GetMapping("/search/capacity")
  public ResponseEntity<List<Venue>> searchByMinCapacity(@RequestParam Integer minCapacity) {
    return ResponseEntity.ok(venueService.searchVenuesByMinCapacity(minCapacity));
  }

  public static class CreateVenueRequest {
    private String name;
    private String location;
    private Integer capacity;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
  }

  public static class UpdateVenueRequest {
    private String name;
    private String location;
    private Integer capacity;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
  }
}
