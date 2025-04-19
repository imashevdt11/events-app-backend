package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record EventListDto(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @JsonProperty("price")
        BigDecimal price,

        @JsonProperty("price_currency")
        String priceCurrency,

        @JsonProperty("amount_of_available_places")
        Integer amountOfAvailablePlaces,

        @JsonProperty("amount_of_taken_places")
        Integer amountOfTakenPlaces,

        @JsonProperty("image_url")
        String imageUrl
) {}