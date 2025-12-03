package com.example.hexagonalapp.infrastructure.mapper;

import com.example.hexagonalapp.application.dto.EventRequest;
import com.example.hexagonalapp.application.dto.EventResponse;
import com.example.hexagonalapp.domain.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Event toDomain(EventRequest request);

    EventResponse toResponse(Event domain);
}