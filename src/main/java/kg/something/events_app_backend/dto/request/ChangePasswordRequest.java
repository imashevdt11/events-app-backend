package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @Pattern(regexp = "^\\d{4}$", message = "Код должен содержать 4 цифры")
        @JsonProperty("code")
        String code,

        @Email(message = "Невалидный формат адреса электронной почты")
        @JsonProperty("email")
        String email,

        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
        @JsonProperty("password")
        String password
) {}
