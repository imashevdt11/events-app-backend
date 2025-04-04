package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse (
    @JsonProperty("first_name")
    String firstName,

    @JsonProperty("last_name")
    String lastName,

    @JsonProperty("phone_number")
    String phoneNumber,

    @JsonProperty("email")
    String email,

    @JsonProperty("image")
    String image
) {}
