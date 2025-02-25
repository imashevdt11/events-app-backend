package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.UserRegistrationDto;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;

public interface UserService {

    UserRegistrationDto registerUser(UserRegistrationDto userRegistrationDto);

    LoginResponse logIn(LoginRequest request);
}
