package kg.something.events_app_backend.service.impl;

import jakarta.transaction.Transactional;
import kg.something.events_app_backend.configuration.JwtUtil;
import kg.something.events_app_backend.dto.UserRegistrationDto;
import kg.something.events_app_backend.dto.request.LoginRequest;
import kg.something.events_app_backend.dto.response.LoginResponse;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.UserRepository;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository repository;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, UserRepository repository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.repository = repository;
    }

    @Transactional
    public UserRegistrationDto registerUser(UserRegistrationDto request) {

        Role role = roleService.findByName(request.role());
        if (role == null) {
            System.out.println("Role not found");
            return null;
        }
        User user;
        if (!repository.existsUserByEmail(request.email())) {
            user = new User(
                    request.firstName(),
                    request.lastName(),
                    request.phoneNumber(),
                    request.email(),
                    passwordEncoder.encode(request.password()),
                    role,
                    false);
            user = repository.save(user);

            return new UserRegistrationDto(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    request.password(),
                    user.getRole().getName()
                    );
        } else {
            System.out.printf("User with email '%s' is already exists", request.email());
            return null;
        }
    }

    @Transactional
    public LoginResponse logIn(LoginRequest request) {

        if (request.email() == null || request.password() == null || request.email().isEmpty() || request.password().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email and password are required");
        }

        Optional<User> user = repository.findUserByEmail(request.email());
        if (user.isPresent()) {

            System.out.println(request.password());
            System.out.println(user.get().getPassword());
            if (!passwordEncoder.matches(request.password(), user.get().getPassword())) {
                throw new ResourceNotFoundException("invalid password");
            }
        } else {
            throw new ResourceNotFoundException("User not found");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var jwtToken = jwtUtil.generateToken(user.get());
        var refreshToken = jwtUtil.generateRefreshToken(user.get());
        return new LoginResponse(jwtToken, refreshToken);
    }
}
