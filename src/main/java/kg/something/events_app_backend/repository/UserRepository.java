package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);

    Boolean existsUserByEmail(String email);
}
