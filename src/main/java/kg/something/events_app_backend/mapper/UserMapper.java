package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getImage() == null ? null : user.getImage().getUrl()
        );
    }
}
