package kg.something.events_app_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PutMapping("/change-user-role/{id}")
    public ResponseEntity<String> changeUserRole(@PathVariable UUID id,
                                                 @RequestParam("role") String role) {
        return new ResponseEntity<>(service.changeUserRole(id, role), HttpStatus.OK);
    }

    @PutMapping("/change-user-status/{id}")
    public ResponseEntity<String> changeUserStatus(@PathVariable UUID id) {
        return new ResponseEntity<>(service.changeUserStatus(id), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserPersonalInfo(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "LOGIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access and refresh tokens received"),
            @ApiResponse(responseCode = "400", description = "Required parameters are not present"),
            @ApiResponse(responseCode = "404", description = "User not found with specified data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.logIn(request));
    }

    @Operation(summary = "REGISTRATION")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User's data saved in database"),
            @ApiResponse(responseCode = "400", description = "Entered data has not been validated"),
            @ApiResponse(responseCode = "409", description = "User with specified data already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserRegistrationRequest request,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>("Ошибка валидации: " + result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.registerUser(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update user's personal info")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserPersonalInfo(@PathVariable("id") UUID id,
                                                               @RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(service.updateUser(id, request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(service.refreshToken(refreshToken));
    }

    @Operation(
            summary = "UPLOAD PROFILE IMAGE",
            description = "Sets an image to user, removes previous image from the Cloudinary",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile image has been uploaded"),
                    @ApiResponse(responseCode = "400", description = "Current request is not a multipart request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid authorization type"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping("/upload-profile-image/{userId}")
    public ResponseEntity<?> uploadProfileImage(@PathVariable("userId") UUID userId,
                                                @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(service.uploadProfileImage(userId, image));
    }

    @Operation(summary = "SUBSCRIBE TO USER")
    @PostMapping("/subscribe/{id}")
    public ResponseEntity<String> subscribeToUser(@PathVariable("id") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.subscribeToUser(userId));
    }

    @Operation(summary = "UNSUBSCRIBE FROM USER")
    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<String> unsubscribeFromUser(@PathVariable("id") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.unsubscribeFromUser(userId));
    }

    @Operation(summary = "USER'S SUBSCRIPTIONS")
    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<List<UserOrganizerDto>> getUserSubscriptions(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllOrganizersUserFollows(id));
    }

    @Operation(summary = "ORGANIZER'S SUBSCRIBERS")
    @GetMapping("/subscribers/{id}")
    public ResponseEntity<List<UserSubscriberDto>> getOrganizerSubscribers(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllOrganizersSubscribers(id));
    }
}
