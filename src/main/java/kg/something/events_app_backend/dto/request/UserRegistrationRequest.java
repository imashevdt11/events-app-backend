package kg.something.events_app_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserRegistrationRequest (

        @Size(min = 2, max = 20, message = "Имя должно содержать от 2 до 20 символов")
        @Pattern(regexp = "^([а-яА-ЯёЁ]+|[a-zA-Z]+)$", message = "Имя должно содержать только буквы из латиницы или кириллицы (смешивание недопустимо)")
        @JsonProperty("first_name")
        String firstName,

        @Size(min = 2, max = 20, message = "Фамилия должна содержать от 2 до 20 символов")
        @Pattern(regexp = "^([а-яА-ЯёЁ]+|[a-zA-Z]+)$", message = "Фамилия должна содержать только буквы из латиницы или кириллицы (смешивание недопустимо)")
        @JsonProperty("last_name")
        String lastName,

        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Номер телефона должен содержать от 10 до 15 цифр ('+' допустим в начале)")
        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("date_of_birth")
        LocalDate dateOfBirth,

        @Email(message = "Невалидный формат адреса электронной почты")
        @JsonProperty("email")
        String email,

        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
        @JsonProperty("password")
        String password,

        @JsonProperty("role")
        String role
) {}
