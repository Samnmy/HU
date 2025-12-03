package com.example.hexagonalapp.infrastructure.mapper;

import com.example.hexagonalapp.domain.model.Event;
import com.example.hexagonalapp.infrastructure.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    EventEntity toEntity(Event domain);

    Event toDomain(EventEntity entity);
}