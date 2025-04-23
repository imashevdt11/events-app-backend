package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record EventUpdateRequest(
        @NotBlank
        @Size(min = 5, max = 50)
        @JsonProperty("title")
        String title,

        @NotBlank
        @JsonProperty("description")
        String description,

        @NotBlank
        @Size(min = 10, max = 100)
        @JsonProperty("location")
        String location,

        @PositiveOrZero
        @JsonProperty("minimum_age")
        Integer minimumAge,

        @NotNull
        @Future(message = "Время мероприятия не может быть перенесено на прошедшее время")
        @JsonProperty("start_time")
        LocalDateTime startTime,

        @PositiveOrZero
        @JsonProperty("price")
        BigDecimal price,

        @Positive
        @JsonProperty("amount_of_places")
        Integer amountOfPlaces,

        @JsonProperty("categories")
        Set<String> categories
) {}
