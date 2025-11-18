package com.tiquetera.eventcatalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    private String description;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede tener más de 50 caracteres")
    @Column(nullable = false)
    private String category;

    @Future(message = "La fecha del evento debe ser futura")
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "ticket_price")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private Double ticketPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private VenueEntity venue;

    // Constructores
    public EventEntity() {}

    public EventEntity(String name, String description, String category,
                       LocalDateTime eventDate, Double ticketPrice, VenueEntity venue) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.ticketPrice = ticketPrice;
        this.venue = venue;
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

    public VenueEntity getVenue() { return venue; }
    public void setVenue(VenueEntity venue) { this.venue = venue; }
}