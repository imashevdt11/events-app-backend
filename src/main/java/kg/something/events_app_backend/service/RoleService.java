package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.dto.RoleListDto;
import kg.something.events_app_backend.dto.response.RoleDetailedResponse;
import kg.something.events_app_backend.dto.response.RoleResponse;
import kg.something.events_app_backend.entity.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    String createRole(RoleDto role);

    String deleteRole(UUID id);

    Role findRoleByName(String name);

    List<RoleResponse> getAllRoles();

    Integer getAmountOfUsersByRole(UUID roleId);

    RoleDetailedResponse getRoleById(UUID id);

    List<RoleListDto> getRolesForList();

    String updateRole(RoleDto roleDto, UUID id);
}
