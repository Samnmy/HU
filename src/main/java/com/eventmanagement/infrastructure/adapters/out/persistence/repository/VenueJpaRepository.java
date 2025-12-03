package com.eventmanagement.infrastructure.adapters.out.persistence.repository;

import com.eventmanagement.infrastructure.adapters.out.persistence.entity.VenueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueJpaRepository extends JpaRepository<VenueEntity, UUID> {

    boolean existsByName(String name);

    List<VenueEntity> findByCity(String city);

    @Query("SELECT v FROM VenueEntity v WHERE " +
            "(:minCapacity IS NULL OR v.capacity >= :minCapacity) AND " +
            "(:maxCapacity IS NULL OR v.capacity <= :maxCapacity)")
    List<VenueEntity> findByCapacityRange(@Param("minCapacity") Integer minCapacity,
                                          @Param("maxCapacity") Integer maxCapacity);

    Page<VenueEntity> findAll(Pageable pageable);
}