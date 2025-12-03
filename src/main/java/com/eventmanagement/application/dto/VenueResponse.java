package com.eventmanagement.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    private UUID id;
    private String name;
    private String location;
    private String city;
    private Integer capacity;
    private Integer eventCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}