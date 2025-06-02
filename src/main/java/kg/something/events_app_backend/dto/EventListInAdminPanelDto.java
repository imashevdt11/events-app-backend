package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventListInAdminPanelDto(
        UUID id,
        String title,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("blocked")
        Boolean blocked,
        @JsonProperty("organizer_full_name")
        String organizerFullName,
        @JsonProperty("organizer_email")
        String organizerEmail,
        @JsonProperty("organizer_phone_number")
        String organizerPhoneNumber
) {}
