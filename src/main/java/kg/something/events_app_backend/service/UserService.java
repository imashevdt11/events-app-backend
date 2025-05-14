package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.UserListDto;
import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String changeUserRole(UUID userId, String roleName);

    String changeUserStatus(UUID userId);

    boolean existsByEmail(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    List<UserSubscriberDto> findAllOrganizersSubscribers(UUID userId);

    List<UserOrganizerDto> findAllOrganizersUserFollows(UUID userId);

    User findUserById(UUID id);

    User findUserByEmail(String email);

    List<User> findUsersByRoleId(UUID roleId);

    List<UserResponse> getAllUsers();

    User getAuthenticatedUser();

    UserResponse getUserById(UUID id);

    List<UserListDto> getUsersForList();

    boolean isAuthenticated();

    String subscribeToUser(UUID userId);

    String unsubscribeFromUser(UUID userId);

    UserResponse updateUser(UUID userId, UserUpdateRequest request);

    String uploadProfileImage(UUID userId, MultipartFile image);

    void save(User user);
}
