package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query(value = """
    SELECT COUNT(*)
    FROM users
    WHERE id_role = :roleId
    """, nativeQuery = true)
    Integer countUsersByRole(@Param("roleId") UUID roleId);

    boolean existsByName(String name);

    Role findRoleByName(String name);

    Role findRoleById(UUID id);
}
