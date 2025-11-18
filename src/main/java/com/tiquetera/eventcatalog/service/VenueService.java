package com.tiquetera.eventcatalog.service;

import com.tiquetera.eventcatalog.dto.VenueDTO;
import com.tiquetera.eventcatalog.entity.VenueEntity;
import com.tiquetera.eventcatalog.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    // Convertir Entity a DTO
    private VenueDTO convertToDTO(VenueEntity entity) {
        return new VenueDTO(
                entity.getId(),
                entity.getName(),
                entity.getCity(),
                entity.getAddress(),
                entity.getMaxCapacity()
        );
    }

    // Convertir DTO a Entity
    private VenueEntity convertToEntity(VenueDTO dto) {
        return new VenueEntity(
                dto.getName(),
                dto.getCity(),
                dto.getAddress(),
                dto.getMaxCapacity()
        );
    }

    // CRUD operations
    public List<VenueDTO> findAll() {
        return venueRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<VenueDTO> findById(Long id) {
        return venueRepository.findById(id)
                .map(this::convertToDTO);
    }

    public VenueDTO save(VenueDTO venueDTO) {
        // Validar nombre único
        if (venueRepository.existsByName(venueDTO.getName())) {
            throw new RuntimeException("Ya existe un venue con el nombre: " + venueDTO.getName());
        }

        VenueEntity entity = convertToEntity(venueDTO);
        VenueEntity savedEntity = venueRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    public Optional<VenueDTO> update(Long id, VenueDTO venueDTO) {
        return venueRepository.findById(id)
                .map(existingVenue -> {
                    // Validar nombre único si cambió
                    if (!existingVenue.getName().equals(venueDTO.getName()) &&
                            venueRepository.existsByName(venueDTO.getName())) {
                        throw new RuntimeException("Ya existe un venue con el nombre: " + venueDTO.getName());
                    }

                    existingVenue.setName(venueDTO.getName());
                    existingVenue.setCity(venueDTO.getCity());
                    existingVenue.setAddress(venueDTO.getAddress());
                    existingVenue.setMaxCapacity(venueDTO.getMaxCapacity());

                    VenueEntity updatedEntity = venueRepository.save(existingVenue);
                    return convertToDTO(updatedEntity);
                });
    }

    public boolean deleteById(Long id) {
        if (venueRepository.existsById(id)) {
            venueRepository.deleteById(id);
            return true;
        }
        return false;
    }
}