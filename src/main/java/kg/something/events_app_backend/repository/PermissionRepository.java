package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    @Query(value = """
    SELECT COUNT(*)
    FROM role_permissions
    WHERE permission_id = :permissionId
    """, nativeQuery = true)
    Integer countRolesByPermission(@Param("permissionId") UUID permissionId);

    boolean existsByName(String name);

    Permission findPermissionById(UUID id);

    Permission findPermissionByName(String name);
}
