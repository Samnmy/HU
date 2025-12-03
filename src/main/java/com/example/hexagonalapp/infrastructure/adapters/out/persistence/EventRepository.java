package com.example.hexagonalapp.infrastructure.adapters.out.persistence;

import com.example.hexagonalapp.infrastructure.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph; // ¡AÑADE ESTE IMPORT!
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>,
        JpaSpecificationExecutor<EventEntity> {

    // Método con JOIN FETCH explícito
    @Query("SELECT e FROM EventEntity e LEFT JOIN FETCH e.venue WHERE e.id = :id")
    Optional<EventEntity> findByIdWithVenue(@Param("id") Long id);

    // Método para obtener todos los eventos con detalles
    @Query("SELECT DISTINCT e FROM EventEntity e " +
            "LEFT JOIN FETCH e.venue " +
            "LEFT JOIN FETCH e.categories")
    List<EventEntity> findAllWithDetails();

    // Método con @EntityGraph para carga eager de relaciones
    @EntityGraph(attributePaths = {"venue", "categories"})
    List<EventEntity> findByStatus(String status);

    // Métodos existentes
    boolean existsByName(String name);

    List<EventEntity> findByVenueId(Long venueId);

    @Query("SELECT e FROM EventEntity e WHERE e.startDate >= :startDate AND e.endDate <= :endDate")
    List<EventEntity> findEventsByDateRange(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}