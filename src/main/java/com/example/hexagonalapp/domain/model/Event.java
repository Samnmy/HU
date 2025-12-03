package com.example.hexagonalapp.domain.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long venueId;
    private LocalDateTime createdAt;

    public boolean isValid() {
        return name != null && !name.trim().isEmpty();
    }
}