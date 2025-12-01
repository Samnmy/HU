package com.example.hexagonalapp.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaEventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

  @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.id = :id")
  EventEntity findByIdWithVenue(@Param("id") Long id);

  @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.venue.id = :venueId")
  List<EventEntity> findByVenueId(@Param("venueId") Long venueId);

  List<EventEntity> findByStatus(EventEntity.EventStatus status);

  @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.startDate BETWEEN :start AND :end")
  List<EventEntity> findByStartDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.startDate > :date")
  List<EventEntity> findByStartDateAfter(@Param("date") LocalDateTime date);

  @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))")
  List<EventEntity> findByTitleContaining(@Param("title") String title);

  @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.venue.id = :venueId AND e.status = :status")
  List<EventEntity> findByVenueIdAndStatus(@Param("venueId") Long venueId, @Param("status") EventEntity.EventStatus status);
}
