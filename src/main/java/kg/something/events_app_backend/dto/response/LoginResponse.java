package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record LoginResponse (
        @JsonProperty
        UUID id,

        @JsonProperty
        String role,

        @JsonProperty
        String accessToken,

        @JsonProperty
        String refreshToken
) {}