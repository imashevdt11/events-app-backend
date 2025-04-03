package kg.something.events_app_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PermissionDto {

    @NotBlank(message = "Название права доступа не должно быть пустым")
    @Size(min = 2, max = 50, message = "Название права доступа должно содержать от 2 до 50 символов")
    private String name;

    public PermissionDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
