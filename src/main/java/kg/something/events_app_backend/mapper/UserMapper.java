package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.Subscription;
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
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getImage() == null ? null : user.getImage().getUrl()
        );
    }

    public User toEntityFromRegistrationRequest(UserRegistrationRequest request, Role role) {
        return new User(
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.dateOfBirth(),
                request.email(),
                passwordEncoder.encode(request.password()),
                role,
                true
        );
    }

    public UserOrganizerDto toUserOrganizerDto(Subscription subscription) {
        return new UserOrganizerDto(
                subscription.getOrganizer().getId(),
                subscription.getOrganizer().getFirstName(),
                subscription.getOrganizer().getLastName(),
                subscription.getOrganizer().getImage() == null ? null : subscription.getOrganizer().getImage().getUrl(),
                subscription.getCreatedAt()
        );
    }

    public UserSubscriberDto toUserSubscriberDto(Subscription subscription) {
        return new UserSubscriberDto(
                subscription.getSubscriber().getId(),
                subscription.getSubscriber().getFirstName(),
                subscription.getSubscriber().getLastName(),
                subscription.getSubscriber().getImage() == null ? null : subscription.getSubscriber().getImage().getUrl(),
                subscription.getCreatedAt()
        );
    }
}
