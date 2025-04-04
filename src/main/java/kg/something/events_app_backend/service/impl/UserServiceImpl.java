package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.configuration.JwtUtil;
import kg.something.events_app_backend.dto.UserRegistrationDto;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.UserMapper;
import kg.something.events_app_backend.repository.UserRepository;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final UserRepository repository;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, UserMapper userMapper, UserRepository repository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var jwtToken = jwtUtil.generateToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponse(user.getId(), jwtToken, refreshToken);
    }

    @Transactional
    public UserRegistrationDto registerUser(UserRegistrationDto request) {

        Role role = roleService.findRoleByName(request.role());
        if (role == null) {
            throw new ResourceNotFoundException("Роль '" + request.role() + "' не найдена");
        }

        User user = new User(
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.email(),
                passwordEncoder.encode(request.password()),
                role,
                false
        );

        user = repository.save(user);

        return new UserRegistrationDto(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                request.password(),
                user.getRole().getName()
        );
    }
}
