package com.eventmanagement.infrastructure.adapters.out.persistence.mapper;

import com.eventmanagement.domain.model.Venue;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersistenceVenueMapper {

    PersistenceVenueMapper INSTANCE = Mappers.getMapper(PersistenceVenueMapper.class);

    VenueEntity toEntity(Venue venue);

    Venue toDomain(VenueEntity entity);
}