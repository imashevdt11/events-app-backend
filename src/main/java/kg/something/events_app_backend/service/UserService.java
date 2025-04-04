package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String changeUserRole(UUID userId, String roleName);

    List<User> findUsersByRoleId(UUID roleId);

    List<User> getAllUsers();

    User getAuthenticatedUser();

    UserResponse getUserById(UUID id);

    LoginResponse logIn(LoginRequest request);

    UserResponse registerUser(UserRegistrationRequest request);

    UserResponse updateUser(UUID userId, UserUpdateRequest request);
}
