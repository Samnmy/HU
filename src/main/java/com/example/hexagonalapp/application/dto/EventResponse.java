package com.example.hexagonalapp.application.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long venueId;
    private LocalDateTime createdAt; // Asegúrate que este campo existe
}