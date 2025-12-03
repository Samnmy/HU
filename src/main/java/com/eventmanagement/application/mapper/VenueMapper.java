package com.eventmanagement.application.mapper;

import com.eventmanagement.application.dto.VenueRequest;
import com.eventmanagement.application.dto.VenueResponse;
import com.eventmanagement.domain.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Venue toDomain(VenueRequest request);

    VenueResponse toResponse(Venue venue);
}