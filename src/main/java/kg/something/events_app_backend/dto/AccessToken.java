package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessToken(
        @JsonProperty
        String accessToken
) {}
