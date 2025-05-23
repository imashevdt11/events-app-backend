package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserListDto(
        UUID id,
        String name,
        String role,
        @JsonProperty("phone_number")
        String phoneNumber,
        String email,
        Boolean enabled,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        String image
) {}
