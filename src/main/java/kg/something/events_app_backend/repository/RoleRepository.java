package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByName(String name);

    Role findRoleByName(String name);

    Role findRoleById(UUID id);

    @Query("SELECT new kg.something.events_app_backend.dto.RoleDto(name) FROM Role")
    List<RoleDto> getAllRolesNames();

}
