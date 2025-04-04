package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getImage() == null ? null : user.getImage().getUrl()
        );
    }

    public User toEntityFromRegistrationRequest(UserRegistrationRequest request, Role role) {
        return new User(
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.email(),
                passwordEncoder.encode(request.password()),
                role,
                false
        );
    }
}
