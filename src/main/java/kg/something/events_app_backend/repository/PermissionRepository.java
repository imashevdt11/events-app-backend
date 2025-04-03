package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    boolean existsByName(String name);

    Permission findPermissionById(UUID id);

    Permission findPermissionByName(String name);

    @Query("SELECT new kg.something.events_app_backend.dto.PermissionDto(name) FROM Permission ")
    List<PermissionDto> getAllPermissionsNames();
}
