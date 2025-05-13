package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kg.something.events_app_backend.dto.CommentDto;
import kg.something.events_app_backend.dto.CategoryDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        String location,
        @JsonProperty("minimum_age")
        Integer minimumAge,
        @JsonProperty("start_time")
        LocalDateTime startTime,
        BigDecimal price,
        @JsonProperty("price_currency")
        String priceCurrency,
        @JsonProperty("amount_of_places")
        Integer amountOfPlaces,
        @JsonProperty("amount_of_available_places")
        Integer amountOfAvailablePlaces,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("image")
        String imageUrl,
        @JsonProperty("categories")
        Set<CategoryDto> categories,
        @JsonProperty("comments")
        Set<CommentDto> comments,
        @JsonProperty("amount_of_likes")
        Integer amountOfLikes,
        @JsonProperty("amount_of_dislikes")
        Integer amountOfDislikes,
        @JsonProperty("isLiked")
        Boolean isLiked,
        @JsonProperty("isDisliked")
        Boolean isDisliked
) {}
