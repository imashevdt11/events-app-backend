package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.dto.PermissionListDto;
import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.PermissionRepository;
import kg.something.events_app_backend.service.PermissionService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final UserService userService;

    public PermissionServiceImpl(PermissionRepository repository, @Lazy UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public String createPermission(PermissionDto permission) {
        User user = userService.getAuthenticatedUser();
        if (repository.existsByName(permission.getName())) {
            throw new ResourceAlreadyExistsException("Право доступа с названием '%s' уже есть в базе данных".formatted(permission.getName()));
        }
        repository.save(new Permission(permission.getName(), user));

        return "Право доступа '%s' сохранено".formatted(permission.getName());
    }

    public String deletePermission(UUID id) {
        Permission permission = repository.findPermissionById(id);
        if (permission == null) {
            throw new ResourceNotFoundException("Право доступа с id '%s' не найдено".formatted(id));
        }
        if (repository.countRolesByPermission(permission.getId()) > 0) {
            throw new InvalidRequestException("Право доступа не может быть удаленна поскольку привязана к мероприятиям");
        }
        repository.delete(permission);

        return "Право доступа '%s' удалено".formatted(permission.getName());
    }

    public Permission findPermissionByName(String name) {
        return repository.findPermissionByName(name);
    }

    public List<Permission> getAllPermissions() {
        return repository.findAll();
    }

    public List<PermissionDto> getAllPermissionsNames() {
        return repository.getAllPermissionsNames();
    }

    public Permission getPermissionById(UUID id) {
        Permission permission = repository.findPermissionById(id);
        if (permission == null) {
            throw new ResourceNotFoundException("Право доступа с id '%s' не найдено".formatted(id));
        }
        return permission;
    }

    public String updatePermission(PermissionDto permissionDto, UUID id) {
        Permission permission = repository.findPermissionById(id);
        if (permission == null) {
            throw new ResourceNotFoundException("Право доступа с id '%s' не найдено".formatted(id));
        }
        String oldPermissionName = permission.getName();
        if (permission.getName().equals(permissionDto.getName())) {
            return "Право доступа не изменено. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(permissionDto.getName())) {
            throw new ResourceAlreadyExistsException("Право доступа с названием '%s' уже есть в базе данных".formatted(permission.getName()));
        }
        permission.setName(permissionDto.getName());
        repository.save(permission);

        return "Название права доступа изменено с '%s' на '%s'".formatted(oldPermissionName, permission.getName());
    }

    public Set<Permission> getExistingPermissions(Set<String> permissionsSet) {
        Set<Permission> permissions = new HashSet<>();
        for (String permissionName: permissionsSet) {
            Permission permission = findPermissionByName(permissionName);
            if (permission == null) {
                throw new ResourceNotFoundException("Право доступа с названием '%s' нет в базе данных'".formatted(permissionName));
            }
            permissions.add(permission);
        }
        return permissions;
    }

    public List<PermissionListDto> getPermissionsForList() {
        return repository.findAll().stream()
                .map(permission ->
                        new PermissionListDto(
                                permission.getId(),
                                permission.getName(),
                                "%s %s".formatted(permission.getUser().getFirstName(), permission.getUser().getLastName()),
                                permission.getCreatedAt(),
                                permission.getUpdatedAt(),
                                getAmountOfRolesByPermission(permission.getId()))
                )
                .toList();
    }

    public Integer getAmountOfRolesByPermission(UUID permissionId) {
        return repository.countRolesByPermission(permissionId);
    }
}
