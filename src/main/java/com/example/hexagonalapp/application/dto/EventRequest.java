package com.example.hexagonalapp.application.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class EventRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long venueId;
}