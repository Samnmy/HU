package com.eventmanagement.integration;

import com.eventmanagement.application.dto.VenueRequest;
import com.eventmanagement.application.dto.AuthRequest;
import com.eventmanagement.application.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VenueControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String organizerToken;

    @BeforeEach
    void setUp() throws Exception {
        // Setup test users and get tokens
        setupTestUsers();
    }

    private void setupTestUsers() throws Exception {
        // Register admin user
        String registerAdminJson = "{\"username\":\"admin_it\",\"email\":\"admin_it@test.com\",\"password\":\"Admin123\",\"role\":\"ADMIN\"}";

        MvcResult adminResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerAdminJson))
                .andReturn();

        String adminResponse = adminResult.getResponse().getContentAsString();
        AuthResponse adminAuth = objectMapper.readValue(adminResponse, AuthResponse.class);
        adminToken = adminAuth.getToken();

        // Register organizer user
        String registerOrganizerJson = "{\"username\":\"organizer_it\",\"email\":\"organizer_it@test.com\",\"password\":\"Organizer123\",\"role\":\"ORGANIZER\"}";

        MvcResult organizerResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerOrganizerJson))
                .andReturn();

        String organizerResponse = organizerResult.getResponse().getContentAsString();
        AuthResponse organizerAuth = objectMapper.readValue(organizerResponse, AuthResponse.class);
        organizerToken = organizerAuth.getToken();
    }

    @Test
    void createVenue_AsAdmin_ShouldSucceed() throws Exception {
        VenueRequest request = new VenueRequest(
                "Test Venue IT " + UUID.randomUUID(),
                "123 Test Street",
                "Test City",
                1000
        );

        mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.location").value(request.getLocation()))
                .andExpect(jsonPath("$.data.capacity").value(request.getCapacity()));
    }

    @Test
    void createVenue_AsOrganizer_ShouldBeForbidden() throws Exception {
        VenueRequest request = new VenueRequest(
                "Test Venue Organizer",
                "456 Organizer Street",
                "Organizer City",
                500
        );

        mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + organizerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createVenue_WithoutAuthentication_ShouldBeForbidden() throws Exception {
        VenueRequest request = new VenueRequest(
                "Test Venue No Auth",
                "789 No Auth Street",
                "No Auth City",
                300
        );

        mockMvc.perform(post("/api/v1/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createVenue_WithDuplicateName_ShouldReturnError() throws Exception {
        VenueRequest request = new VenueRequest(
                "Duplicate Venue IT",
                "Duplicate Street",
                "Duplicate City",
                1000
        );

        // First creation
        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Second creation with same name
        mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    void createVenue_WithInvalidData_ShouldReturnValidationError() throws Exception {
        VenueRequest request = new VenueRequest(
                "",  // Empty name
                "",  // Empty location
                "",  // Empty city
                0    // Invalid capacity
        );

        mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getAllVenues_ShouldReturnVenues() throws Exception {
        // First create some venues
        for (int i = 0; i < 3; i++) {
            VenueRequest request = new VenueRequest(
                    "List Venue " + i + " " + UUID.randomUUID(),
                    "Street " + i,
                    "City " + i,
                    100 * (i + 1)
            );

            mockMvc.perform(post("/api/v1/venues")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        // Get all venues
        mockMvc.perform(get("/api/v1/venues")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void getVenueById_WithExistingId_ShouldReturnVenue() throws Exception {
        // Create a venue
        VenueRequest createRequest = new VenueRequest(
                "Get Venue IT",
                "Get Street",
                "Get City",
                1500
        );

        String createResponse = mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract venue ID from response
        String venueId = objectMapper.readTree(createResponse)
                .path("data")
                .path("id")
                .asText();

        // Get venue by ID
        mockMvc.perform(get("/api/v1/venues/" + venueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(venueId))
                .andExpect(jsonPath("$.data.name").value("Get Venue IT"));
    }

    @Test
    void getVenueById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        UUID nonExistingId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/venues/" + nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void updateVenue_AsAdmin_ShouldSucceed() throws Exception {
        // Create a venue
        VenueRequest createRequest = new VenueRequest(
                "Update Venue Original",
                "Original Street",
                "Original City",
                1000
        );

        String createResponse = mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String venueId = objectMapper.readTree(createResponse)
                .path("data")
                .path("id")
                .asText();

        // Update the venue
        VenueRequest updateRequest = new VenueRequest(
                "Update Venue Modified",
                "Modified Street",
                "Modified City",
                2000
        );

        mockMvc.perform(put("/api/v1/venues/" + venueId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Update Venue Modified"))
                .andExpect(jsonPath("$.data.capacity").value(2000));
    }

    @Test
    void deleteVenue_AsAdmin_ShouldSucceed() throws Exception {
        // Create a venue
        VenueRequest createRequest = new VenueRequest(
                "Delete Venue IT",
                "Delete Street",
                "Delete City",
                1000
        );

        String createResponse = mockMvc.perform(post("/api/v1/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String venueId = objectMapper.readTree(createResponse)
                .path("data")
                .path("id")
                .asText();

        // Delete the venue
        mockMvc.perform(delete("/api/v1/venues/" + venueId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("deleted")));

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/venues/" + venueId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVenuesByCity_ShouldReturnFilteredVenues() throws Exception {
        // Create venues in different cities
        VenueRequest request1 = new VenueRequest(
                "City Venue 1",
                "Street 1",
                "New York",
                1000
        );

        VenueRequest request2 = new VenueRequest(
                "City Venue 2",
                "Street 2",
                "New York",
                2000
        );

        VenueRequest request3 = new VenueRequest(
                "City Venue 3",
                "Street 3",
                "Los Angeles",
                1500
        );

        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request3)));

        // Get venues by city
        mockMvc.perform(get("/api/v1/venues/city/New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].city").value("New York"))
                .andExpect(jsonPath("$.data[1].city").value("New York"));
    }

    @Test
    void getVenuesByCapacity_ShouldReturnFilteredVenues() throws Exception {
        // Create venues with different capacities
        VenueRequest request1 = new VenueRequest(
                "Capacity Venue 1",
                "Street 1",
                "Test City",
                500
        );

        VenueRequest request2 = new VenueRequest(
                "Capacity Venue 2",
                "Street 2",
                "Test City",
                1000
        );

        VenueRequest request3 = new VenueRequest(
                "Capacity Venue 3",
                "Street 3",
                "Test City",
                1500
        );

        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        mockMvc.perform(post("/api/v1/venues")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request3)));

        // Get venues by capacity range
        mockMvc.perform(get("/api/v1/venues/capacity")
                        .param("minCapacity", "600")
                        .param("maxCapacity", "1200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].capacity").value(1000));
    }
}