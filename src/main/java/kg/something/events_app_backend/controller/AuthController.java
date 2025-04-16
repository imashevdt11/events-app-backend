package kg.something.events_app_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.AccessToken;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Аутентификация пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запрос успешно обработан"),
            @ApiResponse(responseCode = "400", description = "В запросе не переданы одно или несколько необходимых значений"),
            @ApiResponse(responseCode = "404", description = "Запись с указанными данными не найдена в базе"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(service.login(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление access токена")
    @PostMapping("/refresh-token")
    public ResponseEntity<AccessToken> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return new ResponseEntity<>(service.refreshToken(refreshToken), HttpStatus.CREATED);
    }

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запись сохранена в базе данных"),
            @ApiResponse(responseCode = "400", description = "Переданные данные не прошли валидацию | В запросе не переданы одно или несколько необходимых значений"),
            @ApiResponse(responseCode = "404", description = "Роль с указанным названием не найдена в базе данных"),
            @ApiResponse(responseCode = "409", description = "Запись с переданным адресом эл. почты (email) и/или номером телефона уже есть в базе данных"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }
}
