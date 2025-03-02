package kg.something.events_app_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {

    @NotBlank(message = "Название категории не должно быть пустым")
    @Size(min = 2, max = 50, message = "Название категории должно содержать от 2 до 50 символов")
    private String name;

    public CategoryDto() {}

    public CategoryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
