package kg.something.events_app_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Название категории не должно быть пустым")
        @Size(min = 2, max = 30, message = "Название категории должно содержать от 2 до 30 символов")
        String name
) {}
