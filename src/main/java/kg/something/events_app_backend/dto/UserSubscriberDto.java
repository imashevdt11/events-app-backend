package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSubscriberDto(
        @JsonProperty("subscriber_id")
        UUID id,
        @JsonProperty("subscriber_first_name")
        String firstName,
        @JsonProperty("subscriber_last_name")
        String lastName,
        @JsonProperty("subscriber_image_url")
        String imageUrl,
        @JsonProperty("subscribed_at")
        LocalDateTime subscribedAt
) {}
