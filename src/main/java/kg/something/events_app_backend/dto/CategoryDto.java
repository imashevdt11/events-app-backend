package kg.something.events_app_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        @NotBlank(message = "Название категории не должно быть пустым")
        @Size(min = 2, max = 20, message = "Название категории должно содержать от 2 до 20 символов")
        @Pattern(
                regexp = "^([а-яА-ЯёЁ]+( [а-яА-ЯёЁ]+)*|[a-zA-Z]+( [a-zA-Z]+)*)$",
                message = "Название должно содержать от 2 до 20 букв кириллицы или латиницы без смешивания. Допустимы пробелы между словами"
        )
        String name
) {}
