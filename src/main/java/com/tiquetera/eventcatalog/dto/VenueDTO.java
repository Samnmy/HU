package com.tiquetera.eventcatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VenueDTO {
    private Long id;

    @NotBlank(message = "El nombre del venue es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 50, message = "La ciudad no puede tener más de 50 caracteres")
    private String city;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String address;

    private Integer maxCapacity;

    // Constructores
    public VenueDTO() {}

    public VenueDTO(Long id, String name, String city, String address, Integer maxCapacity) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.maxCapacity = maxCapacity;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}