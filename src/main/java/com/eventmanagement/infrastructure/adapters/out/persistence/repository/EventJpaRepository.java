package com.eventmanagement.infrastructure.adapters.out.persistence.repository;

import com.eventmanagement.infrastructure.adapters.out.persistence.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {

    boolean existsByName(String name);

    List<EventEntity> findByVenueId(UUID venueId);

    List<EventEntity> findByStatus(String status);

    @Query("SELECT e FROM EventEntity e WHERE e.startDate >= :start AND e.endDate <= :end")
    List<EventEntity> findByDateRange(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    @Query("SELECT e FROM EventEntity e JOIN e.venue v WHERE v.city = :city")
    List<EventEntity> findByCity(@Param("city") String city);

    Page<EventEntity> findAll(Pageable pageable);
}