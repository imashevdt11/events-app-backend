package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.AccessToken;
import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String changeUserRole(UUID userId, String roleName);

    String changeUserStatus(UUID userId);

    List<User> findUsersByRoleId(UUID roleId);

    List<User> getAllUsers();

    User getAuthenticatedUser();

    boolean isAuthenticated();

    UserResponse getUserById(UUID id);

    LoginResponse logIn(LoginRequest request);

    AccessToken refreshToken(String refreshToken);

    UserResponse registerUser(UserRegistrationRequest request);

    UserResponse updateUser(UUID userId, UserUpdateRequest request);

    String uploadProfileImage(UUID userId, MultipartFile image);

    String subscribeToUser(UUID userId);

    String unsubscribeFromUser(UUID userId);

    List<UserOrganizerDto> findAllOrganizersUserFollows(UUID userId);

    List<UserSubscriberDto> findAllOrganizersSubscribers(UUID userId);

    User findUserById(UUID id);
}
