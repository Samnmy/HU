package com.tiquetera.eventcatalog.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class EventDTO {
    private Long id;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    private String description;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede tener más de 50 caracteres")
    private String category;

    @Future(message = "La fecha del evento debe ser futura")
    private LocalDateTime eventDate;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private Double ticketPrice;

    @NotNull(message = "El venue es obligatorio")
    private Long venueId;

    private String venueName;

    // Constructores
    public EventDTO() {}

    public EventDTO(Long id, String name, String description, String category,
                    LocalDateTime eventDate, Double ticketPrice, Long venueId, String venueName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.ticketPrice = ticketPrice;
        this.venueId = venueId;
        this.venueName = venueName;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public Double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(Double ticketPrice) { this.ticketPrice = ticketPrice; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
}