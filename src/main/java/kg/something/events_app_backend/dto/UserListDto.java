package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserListDto(
        UUID id,
        String name,
        String role,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {}
