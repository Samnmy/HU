package com.eventmanagement.application.mapper;

import com.eventmanagement.application.dto.EventRequest;
import com.eventmanagement.application.dto.EventResponse;
import com.eventmanagement.domain.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Event toDomain(EventRequest request);

    EventResponse toResponse(Event event);
}