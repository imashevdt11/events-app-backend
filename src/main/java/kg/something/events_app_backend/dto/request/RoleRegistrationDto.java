package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RoleRegistrationDto(
        @JsonProperty("name")
        String name
) {}