package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);
}
