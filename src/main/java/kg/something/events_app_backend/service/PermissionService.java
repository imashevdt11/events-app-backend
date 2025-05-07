package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.dto.PermissionListDto;
import kg.something.events_app_backend.entity.Permission;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PermissionService {

    String createPermission(PermissionDto permission);

    Permission findPermissionByName(String name);

    String deletePermission(UUID id);

    List<PermissionDto> getAllPermissionsNames();

    List<Permission> getAllPermissions();

    Permission getPermissionById(UUID id);

    String updatePermission(PermissionDto permissionDto, UUID id);

    Set<Permission> getExistingPermissions(Set<String> permissionsSet);

    List<PermissionListDto> getPermissionsForList();
}
