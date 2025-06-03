package kg.something.events_app_backend.configuration;

import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.repository.PermissionRepository;
import kg.something.events_app_backend.repository.RoleRepository;
import kg.something.events_app_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("diyasimashev211@gmail.com")) {
            return;
        }
        Permission permission = null;
        Role role;
        if (roleRepository.existsByName("ROLE_ADMIN")) {
            role = roleRepository.findRoleByName("ROLE_ADMIN");
        } else {
            if (permissionRepository.existsByName("EVERYTHING")) {
                permission = permissionRepository.findPermissionByName("EVERYTHING");
            } else {
                permission = new Permission("EVERYTHING");
                permissionRepository.save(permission);
            }
            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            role = new Role("ROLE_ADMIN", permissions);
            roleRepository.save(role);
        }

        User user = new User(
                "Diyas",
                "Imashev",
                "07005911119",
                LocalDate.of(2004,3, 11),
                "diyasimashev211@gmail.com",
                passwordEncoder.encode("admin"),
                role,
                true);
        userRepository.save(user);

        if (permission != null) {
            permission.setUser(user);
            permissionRepository.save(permission);
        }
        role.setUser(user);
        roleRepository.save(role);
    }
}

