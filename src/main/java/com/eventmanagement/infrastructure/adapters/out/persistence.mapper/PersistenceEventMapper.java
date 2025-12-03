package com.eventmanagement.infrastructure.adapters.out.persistence.mapper;

import com.eventmanagement.domain.model.Event;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {PersistenceVenueMapper.class, PersistenceCategoryMapper.class})
public interface PersistenceEventMapper {

    PersistenceEventMapper INSTANCE = Mappers.getMapper(PersistenceEventMapper.class);

    @Mapping(target = "venue", source = "venue")
    @Mapping(target = "categories", source = "categories")
    EventEntity toEntity(Event event);

    @Mapping(target = "venue", source = "venue")
    @Mapping(target = "categories", source = "categories")
    Event toDomain(EventEntity entity);
}