package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse (
        @JsonProperty
        String accessToken,

        @JsonProperty
        String refreshToken
) {}