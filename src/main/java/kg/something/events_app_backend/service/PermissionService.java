package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.dto.PermissionListDto;
import kg.something.events_app_backend.dto.response.PermissionDetailedResponse;
import kg.something.events_app_backend.dto.response.PermissionResponse;
import kg.something.events_app_backend.entity.Permission;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PermissionService {

    String createPermission(PermissionDto permission);

    String deletePermission(UUID id);

    Permission findPermissionByName(String name);

    List<PermissionResponse> getAllPermissions();

    PermissionDetailedResponse getPermissionById(UUID id);

    String updatePermission(PermissionDto permissionDto, UUID id);

    Set<Permission> getExistingPermissions(Set<String> permissionsSet);

    List<PermissionListDto> getPermissionsForList();
}
