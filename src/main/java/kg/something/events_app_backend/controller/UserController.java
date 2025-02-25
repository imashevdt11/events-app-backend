package kg.something.events_app_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.UserRegistrationDto;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "REGISTRATION",
            description = "Accepts user's data. Returns http-status result and saved data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User's data saved in database"),
                    @ApiResponse(responseCode = "400", description = "Entered data has not been validated"),
                    @ApiResponse(responseCode = "409", description = "User with specified data already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody UserRegistrationDto request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getAllErrors());
        }
        UserRegistrationDto userRegistrationDto = service.registerUser(request);
        if (userRegistrationDto == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(request));
    }

    @Operation(
            summary = "LOGIN",
            description = "Accepts email and password. Returns access and refresh tokens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Access and refresh tokens received"),
                    @ApiResponse(responseCode = "400", description = "Required parameters are not present"),
                    @ApiResponse(responseCode = "404", description = "User not found with specified data"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.logIn(request));
    }
}
