package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.dto.UserListDto;
import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.Subscription;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.UserMapper;
import kg.something.events_app_backend.repository.UserRepository;
import kg.something.events_app_backend.service.CloudinaryService;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.SubscriptionService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository repository;
    private final CloudinaryService cloudinaryService;
    private final RoleService roleService;
    private final SubscriptionService subscriptionService;

    public UserServiceImpl(CloudinaryService cloudinaryService, RoleService roleService, UserMapper userMapper, UserRepository repository, SubscriptionService subscriptionService) {
        this.userMapper = userMapper;
        this.repository = repository;
        this.cloudinaryService = cloudinaryService;
        this.roleService = roleService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String changeUserRole(UUID userId, String roleName) {
        User user = findUserById(userId);
        Role role = roleService.findRoleByName(roleName);

        user.setRole(role);
        repository.save(user);

        return "Роль пользователя '%s %s' изменена на '%s'".formatted(user.getFirstName(), user.getLastName(), roleName);
    }

    @Override
    public String changeUserStatus(UUID userId) {
        User user = findUserById(userId);

        user.setEnabled(!user.isEnabled());
        repository.save(user);

        if (user.isEnabled()) {
            return "Учетная запись пользователя '%s %s' активирована".formatted(user.getFirstName(), user.getLastName());
        }
        return "Учетная запись пользователя '%s %s' заблокирована".formatted(user.getFirstName(), user.getLastName());
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public List<UserSubscriberDto> findAllOrganizersSubscribers(UUID userId) {
        User user = findUserById(userId);
        return subscriptionService.findAllOrganizersSubscribers(user)
                .stream()
                .map(userMapper::toUserSubscriberDto)
                .toList();
    }

    @Override
    public List<UserOrganizerDto> findAllOrganizersUserFollows(UUID userId) {
        User user = findUserById(userId);
        return subscriptionService.findAllOrganizersUserFollows(user)
                .stream()
                .map(userMapper::toUserOrganizerDto)
                .toList();
    }

    @Override
    public User findUserByEmail(String email) {
        return Optional.ofNullable(repository.findUsersByEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email '%s' не найден в базе данных".formatted(email)));
    }

    @Override
    public User findUserById(UUID id) {
        return Optional.ofNullable(repository.findUserById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id '%s' не найден в базе данных".formatted(id)));
    }

    @Override
    public List<User> findUsersByRoleId(UUID roleId) {
        List<User> user = repository.findUsersByRole_Id(roleId);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователей с указанной ролью не найдены");
        }
        return user;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с почтой '" + email + "' не найден");
        }
        return user;
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = repository.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с id '%s' не найден".formatted(id));
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserListDto> getUsersForList() {
        return repository.findAll().stream()
                .map(user ->
                        new UserListDto(
                                user.getId(),
                                "%s %s".formatted(user.getFirstName(), user.getLastName()),
                                user.getRole().getName(),
                                user.getPhoneNumber(),
                                user.getEmail(),
                                user.getEnabled(),
                                user.getCreatedAt(),
                                user.getImage() == null ? "https://res.cloudinary.com/dn0akydmv/image/upload/v1743943629/dg950peh7y1hj82svuvt.jpg": user.getImage().getUrl()
                        ))
                .toList();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public String subscribeToUser(UUID userId) {
        User subscriber = getAuthenticatedUser();
        User organizer = findUserById(userId);

        if (subscriber.getId().equals(organizer.getId())) {
            throw new InvalidRequestException("Пользователь не может подписаться на самого себя");
        }
        if (!subscriber.getRole().getName().equals("ROLE_ORGANIZER")) {
            throw new InvalidRequestException("Вы не можете подписаться на пользователя, поскольку он не является организатором");
        }
        Subscription subscription = subscriptionService.findSubscribeByOrganizerAndSubscriber(organizer, subscriber);
        if (subscription != null) {
            throw new ResourceAlreadyExistsException("Пользователь уже подписан на организатора");
        }
        subscriptionService.save(new Subscription(organizer, subscriber));

        return "Пользователь '%s %s' подписался на '%s %s'"
                .formatted(
                        subscriber.getFirstName(),
                        subscriber.getLastName(),
                        organizer.getFirstName(),
                        organizer.getLastName()
                );
    }

    @Override
    public String unsubscribeFromUser(UUID userId) {
        User subscriber = getAuthenticatedUser();
        User organizer = findUserById(userId);

        Subscription subscription = subscriptionService.findSubscribeByOrganizerAndSubscriber(organizer, subscriber);
        if (subscription == null) {
            throw new ResourceAlreadyExistsException("Пользователь не подписан на организатора");
        }
        subscriptionService.delete(subscription);

        return "Пользователь '%s %s' отписался от '%s %s'"
                .formatted(
                        subscriber.getFirstName(),
                        subscriber.getLastName(),
                        organizer.getFirstName(),
                        organizer.getLastName()
                );
    }

    @Override
    public UserResponse updateUser(UUID userId, UserUpdateRequest request) {
        User user = findUserById(userId);
        checkUniqueFieldsForExistingBeforeUpdate(user, request);

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setDateOfBirth(request.dateOfBirth());
        repository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public String uploadProfileImage(UUID userId, MultipartFile image) {
        User user = getAuthenticatedUser();
        String imageUrl;
        try {
            imageUrl = cloudinaryService.uploadImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (user.getImage() != null) {
            if (!user.getImage().getUrl().isEmpty()) {
                try {
                    cloudinaryService.deleteImage(user.getImage().getUrl());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user.getImage().setUrl(imageUrl);
        } else {
            user.setImage(new Image(imageUrl));
        }
        repository.save(user);

        return "Изображение профиля сохранено";
    }

    private void checkUniqueFieldsForExistingBeforeUpdate(User existingUser, UserUpdateRequest updateRequest) {
        if (repository.existsByEmail(updateRequest.email()) && !existingUser.getEmail().equals(updateRequest.email())) {
            throw new ResourceAlreadyExistsException("Пользователь с почтой '%s' уже есть в базе данных".formatted(updateRequest.email()));
        }
        if (repository.existsByPhoneNumber(updateRequest.phoneNumber()) && !existingUser.getPhoneNumber().equals(updateRequest.phoneNumber())) {
            throw new ResourceAlreadyExistsException("Пользователь с номером телефона '%s' уже есть в базе данных".formatted(updateRequest.phoneNumber()));
        }
    }
}
