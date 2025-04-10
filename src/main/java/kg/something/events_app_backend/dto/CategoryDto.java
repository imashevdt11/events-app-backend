package kg.something.events_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto (
        @JsonProperty("name")
        @NotBlank(message = "Название категории не должно быть пустым")
        @Size(min = 2, max = 50, message = "Название категории должно содержать от 2 до 50 символов")
        String name
) {}
