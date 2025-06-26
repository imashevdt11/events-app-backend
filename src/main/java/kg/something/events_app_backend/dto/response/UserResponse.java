package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse (
        UUID id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("phone_number")
        String phoneNumber,
        @JsonProperty("date_of_birth")
        LocalDate dateOfBirth,
        String email,
        String image,
        String role
) {}
