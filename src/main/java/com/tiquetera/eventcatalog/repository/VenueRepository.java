package com.tiquetera.eventcatalog.repository;

import com.tiquetera.eventcatalog.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
    Optional<VenueEntity> findByName(String name);
    boolean existsByName(String name);
}