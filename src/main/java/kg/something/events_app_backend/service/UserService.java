package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.UserRegistrationDto;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> getAllUsers();

    User getAuthenticatedUser();

    UserResponse getUserById(UUID id);

    LoginResponse logIn(LoginRequest request);

    UserRegistrationDto registerUser(UserRegistrationDto userRegistrationDto);

    List<User> findUsersByRoleId(UUID roleId);
}
