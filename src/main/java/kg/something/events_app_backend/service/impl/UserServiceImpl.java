package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.configuration.JwtUtil;
import kg.something.events_app_backend.dto.AccessToken;
import kg.something.events_app_backend.dto.UserOrganizerDto;
import kg.something.events_app_backend.dto.UserSubscriberDto;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.Subscription;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.OutOfDateException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.UserMapper;
import kg.something.events_app_backend.repository.UserRepository;
import kg.something.events_app_backend.service.CloudinaryService;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.SubscriptionService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final CloudinaryService cloudinaryService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final UserRepository repository;
    private final SubscriptionService subscriptionService;

    public UserServiceImpl(AuthenticationManager authenticationManager, CloudinaryService cloudinaryService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, UserDetailsService userDetailsService, UserMapper userMapper, UserRepository repository, SubscriptionService subscriptionService) {
        this.authenticationManager = authenticationManager;
        this.cloudinaryService = cloudinaryService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.repository = repository;
        this.subscriptionService = subscriptionService;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public String changeUserRole(UUID userId, String roleName) {
        User user = findUserById(userId);
        Role role = roleService.findRoleByName(roleName);

        user.setRole(role);
        repository.save(user);

        return "Роль пользователя '%s %s' изменена на '%s'".formatted(user.getFirstName(), user.getLastName(), roleName);
    }

    public String changeUserStatus(UUID userId) {
        User user = findUserById(userId);

        user.setEnabled(!user.isEnabled());
        repository.save(user);

        if (user.isEnabled()) {
            return "Учетная запись пользователя '%s %s' активирована".formatted(user.getFirstName(), user.getLastName());
        }
        return "Учетная запись пользователя '%s %s' заблокирована".formatted(user.getFirstName(), user.getLastName());
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с почтой '" + email + "' не найден");
        }
        return user;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    public UserResponse getUserById(UUID id) {
        User user = repository.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с id " + id + " не найден");
        }
        return userMapper.toUserResponse(user);
    }

    public List<User> findUsersByRoleId(UUID roleId) {
        List<User> user = repository.findUsersByRole_Id(roleId);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователей с ролью " + roleId + " не найдено");
        }
        return user;
    }

    @Transactional
    public LoginResponse logIn(LoginRequest request) {
        if (request.email() == null || request.password() == null || request.email().isEmpty() || request.password().isEmpty()) {
            throw new InvalidRequestException("Введите почту и пароль");
        }
        User user = repository.findByEmail(request.email());
        if (user != null) {
            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new ResourceNotFoundException("Неправильный пароль");
            }
        } else {
            throw new ResourceNotFoundException("Пользователь с почтой '" + request.email() + "' не найден");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var jwtToken = jwtUtil.generateToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponse(user.getId(), jwtToken, refreshToken);
    }

    public AccessToken refreshToken(String refreshToken) {
        if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
            throw new OutOfDateException("Срок действия токена истек");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(userDetails);

        return new AccessToken(newAccessToken);
    }

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        checkUniqueFieldsForExistingBeforeRegistration(request);
        Role role = roleService.findRoleByName(request.role());

        User user = userMapper.toEntityFromRegistrationRequest(request, role);
        repository.save(user);

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(UUID userId, UserUpdateRequest request) {
        User user = findUserById(userId);
        checkUniqueFieldsForExistingBeforeUpdate(user, request);

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        repository.save(user);

        return userMapper.toUserResponse(user);
    }

    private void checkUniqueFieldsForExistingBeforeUpdate(User existingUser, UserUpdateRequest updateRequest) {
        if (repository.existsByEmail(updateRequest.email()) && !existingUser.getEmail().equals(updateRequest.email())) {
            throw new ResourceAlreadyExistsException("Пользователь с почтой '%s' уже есть в базе данных".formatted(updateRequest.email()));
        }
        if (repository.existsByPhoneNumber(updateRequest.phoneNumber()) && !existingUser.getPhoneNumber().equals(updateRequest.phoneNumber())) {
            throw new ResourceAlreadyExistsException("Пользователь с номером телефона '%s' уже есть в базе данных".formatted(updateRequest.phoneNumber()));
        }
    }

    private void checkUniqueFieldsForExistingBeforeRegistration(UserRegistrationRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Пользователь с почтой '%s' уже есть в базе данных".formatted(request.email()));
        }
        if (repository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ResourceAlreadyExistsException("Пользователь с номером телефона '%s' уже есть в базе данных".formatted(request.phoneNumber()));
        }
    }

    private User findUserById(UUID id) {
        return Optional.ofNullable(repository.findUserById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id '%s' не найден в базе данных".formatted(id)));
    }

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

    public String subscribeToUser(UUID userId) {
        User subscriber = getAuthenticatedUser();
        User organizer = findUserById(userId);

        if (subscriber.getId().equals(organizer.getId())) {
            throw new InvalidRequestException("Пользователь не может подписаться на самого себя");
        }
//        if (subscriber.getRole().getName().equals("ROLE_ORGANIZER")) {
//            throw new InvalidRequestException("Вы не можете подписаться на пользователя, поскольку он не является организатором");
//        }
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

    public List<UserOrganizerDto> findAllOrganizersUserFollows(UUID userId) {
        User user = getAuthenticatedUser();
        return subscriptionService.findAllOrganizersUserFollows(user)
                .stream()
                .map(userMapper::toUserOrganizerDto)
                .toList();
    }

    public List<UserSubscriberDto> findAllOrganizersSubscribers(UUID userId) {
        User user = getAuthenticatedUser();
        return subscriptionService.findAllOrganizersSubscribers(user)
                .stream()
                .map(userMapper::toUserSubscriberDto)
                .toList();
    }
}
