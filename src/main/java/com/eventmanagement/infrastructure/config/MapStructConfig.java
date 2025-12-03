package com.eventmanagement.infrastructure.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {

    @Bean
    public com.eventmanagement.application.mapper.EventMapper eventMapper() {
        return Mappers.getMapper(com.eventmanagement.application.mapper.EventMapper.class);
    }

    @Bean
    public com.eventmanagement.application.mapper.VenueMapper venueMapper() {
        return Mappers.getMapper(com.eventmanagement.application.mapper.VenueMapper.class);
    }
}
