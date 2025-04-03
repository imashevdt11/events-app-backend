package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.PermissionDto;
import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.PermissionRepository;
import kg.something.events_app_backend.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;

    public PermissionServiceImpl(PermissionRepository repository) {
        this.repository = repository;
    }

    public String createPermission(PermissionDto permission) {
        if (repository.existsByName(permission.getName())) {
            throw new ResourceAlreadyExistsException("Право доступа с названием '" + permission.getName() + "' уже есть в базе данных");
        }
        repository.save(new Permission(permission.getName()));

        return "Право доступа '" + permission.getName() + "' сохранено";
    }

    public String deletePermission(UUID id) {
        Permission permission = repository.findPermissionById(id);
        if (permission == null) {
            throw new ResourceNotFoundException("Право доступа с id " + id + " не найдено");
        }
        repository.delete(permission);

        return "Право доступа '" + permission.getName() + "' удалено";
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
            throw new ResourceNotFoundException("Право доступа с id " + id + " не найдено");
        }
        return permission;
    }

    public String updatePermission(PermissionDto permissionDto, UUID id) {
        Permission permission = repository.findPermissionById(id);
        if (permission == null) {
            throw new ResourceNotFoundException("Право доступа с id " + id + " не найдено");
        }

        String oldPermissionName = permission.getName();
        if (permission.getName().equals(permissionDto.getName())) {
            return "Право доступа не изменена. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(permissionDto.getName())) {
            throw new ResourceAlreadyExistsException("Право доступа с названием '" + permission.getName() + "' уже есть в базе данных");
        }
        permission.setName(permissionDto.getName());
        repository.save(permission);

        return "Название права доступа изменено с '" + oldPermissionName + "' на '" + permission.getName() + "'";
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
}
