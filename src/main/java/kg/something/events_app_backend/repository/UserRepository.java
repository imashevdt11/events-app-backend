package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);

    User findUserById(UUID id);

    User findByEmail(String email);

    List<User> findUsersByRole_Id(UUID roleId);
}
