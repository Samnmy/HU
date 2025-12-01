package com.example.hexagonalapp.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JpaVenueRepository extends JpaRepository<VenueEntity, Long> {
  List<VenueEntity> findByActiveTrue();

  @Query("SELECT v FROM VenueEntity v LEFT JOIN FETCH v.events WHERE v.id = :id")
  VenueEntity findByIdWithEvents(@Param("id") Long id);

  List<VenueEntity> findByLocationContaining(String location);
  List<VenueEntity> findByCapacityGreaterThanEqual(Integer capacity);

  @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"events"})
  List<VenueEntity> findAll();
}
