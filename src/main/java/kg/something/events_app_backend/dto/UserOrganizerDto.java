package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserOrganizerDto(
        @JsonProperty("organizer_id")
        UUID id,

        @JsonProperty("organizer_first_name")
        String firstName,

        @JsonProperty("organizer_last_name")
        String lastName,

        @JsonProperty("organizer_image_url")
        String imageUrl,

        @JsonProperty("subscribed_at")
        LocalDateTime subscribedAt
) {}
