package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.configuration.JwtUtil;
import kg.something.events_app_backend.dto.AccessToken;
import kg.something.events_app_backend.dto.request.ChangePasswordRequest;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.request.UserRegistrationRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.dto.response.UserResponse;
import kg.something.events_app_backend.entity.ConfirmationCode;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.OutOfDateException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.UserMapper;
import kg.something.events_app_backend.service.AuthService;
import kg.something.events_app_backend.service.ConfirmationCodeService;
import kg.something.events_app_backend.service.EmailService;
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
    private final ConfirmationCodeService confirmationCodeService;
    private final EmailService emailService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, UserDetailsService userDetailsService, UserMapper userMapper, UserService userService, ConfirmationCodeService confirmationCodeService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.userService = userService;
        this.confirmationCodeService = confirmationCodeService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public String changePassword(ChangePasswordRequest request) {

        User user = userService.findUserByEmail(request.email());
        ConfirmationCode confirmationCode = confirmationCodeService.findByUserAndCode(user, Integer.parseInt(request.code()));

        if (confirmationCode.isExpired()) {
            throw new OutOfDateException("Срок действия кода подтверждения истек");
        }
        if (!confirmationCode.getCode().equals(Integer.parseInt(request.code()))) {
            throw new ResourceNotFoundException("Код подтверждения '%s' не найден");
        }
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(request.password()));
        userService.save(user);
        confirmationCodeService.delete(confirmationCode);

        return "Пароль изменен";
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
        return new LoginResponse(user.getId(), user.getRole() != null ? user.getRole().getName() : "", jwtToken, refreshToken);
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

    @Override
    public String sendRestoreCode(String email) {
        User user = userService.findUserByEmail(email);
        ConfirmationCode confirmationCode = confirmationCodeService.findConfirmationCodeByUser(user);
        emailService.sendEmailWithConfirmationCode(confirmationCode, user);

        return "Код подтверждения отправлен на почту '%s'".formatted(email);
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
