package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kg.something.events_app_backend.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record EventResponse(

        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @JsonProperty("location")
        String location,

        @JsonProperty("minimum_age")
        Integer minimumAge,

        @JsonProperty("start_time")
        LocalDateTime startTime,

        @JsonProperty("price")
        BigDecimal price,

        @JsonProperty("price_currency")
        String priceCurrency,

        @JsonProperty("amount_of_places")
        Integer amountOfPlaces,

        @JsonProperty("amount_of_available_places")
        Integer amountOfAvailablePlaces,

        @JsonProperty("image")
        String imageUrl,

        @JsonProperty("categories")
        Set<Category> categories
) {
}
