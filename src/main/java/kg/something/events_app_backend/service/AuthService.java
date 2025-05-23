package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.AccessToken;
import kg.something.events_app_backend.dto.request.ChangePasswordRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;

public interface AuthService {

    String changePassword(ChangePasswordRequest request);

    LoginResponse login(LoginRequest request);

    AccessToken refreshToken(String refreshToken);

    UserResponse register(UserRegistrationRequest request);

    String sendRestoreCode(String email);
}
