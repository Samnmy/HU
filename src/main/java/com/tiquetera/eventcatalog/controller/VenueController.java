package com.tiquetera.eventcatalog.controller;

import com.tiquetera.eventcatalog.dto.VenueDTO;
import com.tiquetera.eventcatalog.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        List<VenueDTO> venues = venueService.findAll();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        Optional<VenueDTO> venue = venueService.findById(id);
        return venue.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createVenue(@Valid @RequestBody VenueDTO venueDTO) {
        try {
            VenueDTO savedVenue = venueService.save(venueDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVenue);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueDTO venueDTO) {
        try {
            Optional<VenueDTO> updatedVenue = venueService.update(id, venueDTO);
            return updatedVenue.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        boolean deleted = venueService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}