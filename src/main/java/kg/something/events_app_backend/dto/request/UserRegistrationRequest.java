package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegistrationRequest (
    @JsonProperty("first_name")
    String firstName,

    @JsonProperty("last_name")
    String lastName,

    @JsonProperty("phone_number")
    String phoneNumber,

    @JsonProperty("email")
    String email,

    @JsonProperty("password")
    String password,

    @JsonProperty("role")
    String role
){}
