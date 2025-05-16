package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
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
public class AuthControllerApi {

    private final AuthService service;

    public AuthControllerApi(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Аутентификация пользователя")
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
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }
}
