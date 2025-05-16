package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.dto.PermissionListDto;
import kg.something.events_app_backend.dto.response.PermissionDetailedResponse;
import kg.something.events_app_backend.dto.response.PermissionResponse;
import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.PermissionMapper;
import kg.something.events_app_backend.repository.PermissionRepository;
import kg.something.events_app_backend.service.PermissionService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final UserService userService;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository repository, @Lazy UserService userService, PermissionMapper permissionMapper) {
        this.repository = repository;
        this.userService = userService;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public String createPermission(PermissionDto permission) {
        User user = userService.getAuthenticatedUser();
        if (repository.existsByName(permission.name())) {
            throw new ResourceAlreadyExistsException("Право доступа с названием '%s' уже есть в базе данных".formatted(permission.name()));
        }
        repository.save(new Permission(permission.name(), user));

        return "Право доступа создано";
    }

    @Override
    public String deletePermission(UUID id) {
        Permission permission = findPermissionById(id);
        if (repository.countRolesByPermission(permission.getId()) > 0) {
            throw new InvalidRequestException("Право доступа не может быть удаленна поскольку привязана к мероприятиям");
        }
        repository.delete(permission);

        return "Право доступа '%s' удалено".formatted(permission.getName());
    }

    public Permission findPermissionById(UUID id) {
        return Optional.ofNullable(repository.findPermissionById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Право доступа с id '%s' не найдено в базе данных".formatted(id)));
    }

    @Override
    public Permission findPermissionByName(String name) {
        return Optional.ofNullable(repository.findPermissionByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Право доступа с названием '%s' не найдено в базе данных".formatted(name)));
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return repository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public Integer getAmountOfRolesByPermission(UUID permissionId) {
        return repository.countRolesByPermission(permissionId);
    }

    @Override
    public Set<Permission> getExistingPermissions(Set<String> permissionsSet) {
        Set<Permission> permissions = new HashSet<>();
        for (String permissionName: permissionsSet) {
            Permission permission = findPermissionByName(permissionName);
            permissions.add(permission);
        }
        return permissions;
    }

    @Override
    public PermissionDetailedResponse getPermissionById(UUID id) {
        Permission permission = findPermissionById(id);
        return permissionMapper.toPermissionDetailedResponse(permission);
    }

    @Override
    public List<PermissionListDto> getPermissionsForList() {
        return repository.findAll().stream()
                .map(permission ->
                        new PermissionListDto(
                                permission.getId(),
                                permission.getName(),
                                permission.getUser() == null ? "-": "%s %s".formatted(permission.getUser().getFirstName(), permission.getUser().getLastName()),
                                permission.getCreatedAt(),
                                permission.getUpdatedAt(),
                                getAmountOfRolesByPermission(permission.getId()))
                )
                .toList();
    }

    @Override
    public String updatePermission(PermissionDto permissionDto, UUID id) {
        Permission permission = findPermissionById(id);
        String oldPermissionName = permission.getName();
        if (permission.getName().equals(permissionDto.name())) {
            return "Право доступа не изменено. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(permissionDto.name())) {
            throw new ResourceAlreadyExistsException("Право доступа с названием '%s' уже есть в базе данных".formatted(permission.getName()));
        }
        permission.setName(permissionDto.name());
        repository.save(permission);

        return "Название права доступа изменено с '%s' на '%s'".formatted(oldPermissionName, permission.getName());
    }
}
