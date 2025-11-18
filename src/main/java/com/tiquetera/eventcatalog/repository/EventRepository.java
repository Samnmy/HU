package com.tiquetera.eventcatalog.repository;

import com.tiquetera.eventcatalog.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByName(String name);
    boolean existsByName(String name);

    // Filtros con paginación
    Page<EventEntity> findByVenueCity(String city, Pageable pageable);
    Page<EventEntity> findByCategory(String category, Pageable pageable);
    Page<EventEntity> findByEventDateAfter(LocalDateTime date, Pageable pageable);

    // Filtros combinados
    @Query("SELECT e FROM EventEntity e WHERE " +
            "(:city IS NULL OR e.venue.city = :city) AND " +
            "(:category IS NULL OR e.category = :category) AND " +
            "(:startDate IS NULL OR e.eventDate >= :startDate)")
    Page<EventEntity> findByFilters(@Param("city") String city,
                                    @Param("category") String category,
                                    @Param("startDate") LocalDateTime startDate,
                                    Pageable pageable);
}