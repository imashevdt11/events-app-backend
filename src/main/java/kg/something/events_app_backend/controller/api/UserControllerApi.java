package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class UserControllerApi {

    private final UserService service;

    public UserControllerApi(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Изменение роли пользователя")
    @PutMapping("/change-user-role/{id}")
    public ResponseEntity<String> changeUserRole(@PathVariable UUID id,
                                                 @RequestParam("role") String role) {
        return new ResponseEntity<>(service.changeUserRole(id, role), HttpStatus.OK);
    }

    @Operation(summary = "Разблокировать/активировать учетную запись пользователя")
    @PutMapping("/change-user-status/{id}")
    public ResponseEntity<String> changeUserStatus(@PathVariable UUID id) {
        return new ResponseEntity<>(service.changeUserStatus(id), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка всех пользователей")
    @GetMapping("/admin")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Operation(summary = "Получение информации о пользователе по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserPersonalInfo(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Изменение данных пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserPersonalInfo(@PathVariable("id") UUID id,
                                                               @RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(service.updateUser(id, request), HttpStatus.OK);
    }

    @Operation(
            summary = "Установка новой фотографии профиля пользователя",
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

    @Operation(summary = "Подписка на пользователя")
    @PostMapping("/subscribe/{id}")
    public ResponseEntity<String> subscribeToUser(@PathVariable("id") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.subscribeToUser(userId));
    }

    @Operation(summary = "Отписка от пользователя")
    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<String> unsubscribeFromUser(@PathVariable("id") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.unsubscribeFromUser(userId));
    }

    @Operation(summary = "Получение списка организаторов, на которых подписан пользователь")
    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<List<UserOrganizerDto>> getUserSubscriptions(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllOrganizersUserFollows(id));
    }

    @Operation(summary = "Получение списка всех подписчиков пользователя")
    @GetMapping("/subscribers/{id}")
    public ResponseEntity<List<UserSubscriberDto>> getOrganizerSubscribers(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllOrganizersSubscribers(id));
    }
}
