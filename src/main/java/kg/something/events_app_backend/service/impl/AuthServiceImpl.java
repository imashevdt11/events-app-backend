package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.configuration.JwtUtil;
import kg.something.events_app_backend.dto.AccessToken;
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
import kg.something.events_app_backend.service.AuthService;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, UserDetailsService userDetailsService, UserMapper userMapper, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        if (!StringUtils.hasText(request.email()) || !StringUtils.hasText(request.password())) {
            throw new InvalidRequestException("Введите почту и пароль");
        }
        User user = userService.findUserByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResourceNotFoundException("Неправильный пароль");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponse(user.getId(), jwtToken, refreshToken);
    }

    @Override
    public AccessToken refreshToken(String refreshToken) {
        if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
            throw new OutOfDateException("Срок действия токена истек");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(userDetails);

        return new AccessToken(newAccessToken);
    }

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (LocalDate.now().getYear() - request.dateOfBirth().getYear() < 14) {
            throw new InvalidRequestException("Пользователю должно быть не меньше 14 лет");
        }
        checkUniqueFieldsForExistingBeforeRegistration(request);
        Role role = roleService.findRoleByName(request.role());

        User user = userMapper.toEntityFromRegistrationRequest(request, role);
        userService.save(user);

        return userMapper.toUserResponse(user);
    }

    private void checkUniqueFieldsForExistingBeforeRegistration(UserRegistrationRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Пользователь с почтой '%s' уже есть в базе данных".formatted(request.email()));
        }
        if (userService.existsByPhoneNumber(request.phoneNumber())) {
            throw new ResourceAlreadyExistsException("Пользователь с номером телефона '%s' уже есть в базе данных".formatted(request.phoneNumber()));
        }
    }
}
