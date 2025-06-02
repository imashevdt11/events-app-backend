package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventDetailedInAdminPanel (
        UUID id,
        String title,
        String description,
        String location,
        @JsonProperty("minimum_age")
        Integer minimumAge,
        @JsonProperty("start_time")
        LocalDateTime startTime,
        String price,
        @JsonProperty("amount_of_places")
        Integer amountOfPlaces,
        @JsonProperty("amount_of_available_places")
        Integer amountOfAvailablePlaces,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("image_url")
        String imageUrl,
        @JsonProperty("blocked")
        Boolean blocked,
        @JsonProperty("organizer_full_name")
        String organizerFullName,
        @JsonProperty("organizer_email")
        String organizerEmail,
        @JsonProperty("organizer_phone_number")
        String organizerPhoneNumber
) {}
