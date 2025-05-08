package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleListDto(
        UUID id,
        String name,
        @JsonProperty("creator_name")
        String creatorName,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        Integer amountOfUsersWithRole
) {}
