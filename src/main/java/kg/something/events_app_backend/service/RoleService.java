package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.entity.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    String createRole(RoleDto role);

    Role findRoleByName(String name);

    String deleteRole(UUID id);

    List<RoleDto> getAllRolesNames();

    List<Role> getAllRoles();

    Role getRoleById(UUID id);

    String updateRole(RoleDto roleDto, UUID id);
}
