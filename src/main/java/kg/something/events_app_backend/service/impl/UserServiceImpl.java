package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.configuration.JwtUtil;
import kg.something.events_app_backend.dto.AccessToken;
import kg.something.events_app_backend.dto.UserUpdateRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.OutOfDateException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.UserMapper;
import kg.something.events_app_backend.repository.UserRepository;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final UserRepository repository;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, UserDetailsService userDetailsService, UserMapper userMapper, UserRepository repository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.repository = repository;
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
}
