package com.eventmanagement.domain.model;

import java.util.Set;
import java.util.UUID;

public class Venue {
    private UUID id;
    private String name;
    private String location;
    private String city;
    private Integer capacity;
    private Set<Event> events;

    public Venue() {}

    public Venue(UUID id, String name, String location, String city, Integer capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.city = city;
        this.capacity = capacity;
    }

    public boolean canAccommodate(int attendees) {
        return capacity >= attendees;
    }

    public boolean hasEvents() {
        return events != null && !events.isEmpty();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Set<Event> getEvents() { return events; }
    public void setEvents(Set<Event> events) { this.events = events; }
}