package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.RoleRepository;
import kg.something.events_app_backend.service.PermissionService;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionService permissionService;
    private final UserService userService;

    public RoleServiceImpl(RoleRepository repository, PermissionService permissionService, @Lazy UserService userService) {
        this.repository = repository;
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @Transactional
    public String createRole(RoleDto role) {
        if (repository.existsByName(role.getName())) {
            throw new ResourceAlreadyExistsException("Роль с названием '" + role.getName() + "' уже есть в базе данных");
        }
        Set<Permission> permissions = permissionService.getExistingPermissions(role.getPermissions());
        repository.save(new Role(role.getName(), permissions));

        return "Роль '" + role.getName() + "' сохранена";
    }

    public String deleteRole(UUID id) {
        Role role = repository.findRoleById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Роль с id " + id + " не найдена");
        }
        if (!userService.findUsersByRoleId(role.getId()).isEmpty()) {
            throw new InvalidRequestException("Удаление роли запрещено. Роль привязана к пользователям");
        }
        role.getPermissions().clear();
        repository.delete(role);

        return "Роль '" + role.getName() + "' удалена";
    }

    public Role findRoleByName(String name) {
        return repository.findRoleByName(name);
    }

    public List<Role> getAllRoles() {
        return repository.findAll();
    }

    public List<RoleDto> getAllRolesNames() {
        return repository.getAllRolesNames();
    }

    public Role getRoleById(UUID id) {
        Role role = repository.findRoleById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Роль с id " + id + " не найдена");
        }
        return role;
    }

    public String updateRole(RoleDto roleDto, UUID id) {
        Role role = repository.findRoleById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Роль с id " + id + " не найдена");
        }

        if (repository.existsByName(roleDto.getName()) && !roleDto.getName().equals(role.getName())) {
            throw new ResourceAlreadyExistsException("Роль с названием '" + roleDto.getName() + "' уже есть в базе данных");
        }
        Set<Permission> permissions = permissionService.getExistingPermissions(roleDto.getPermissions());

        role.setName(roleDto.getName());
        role.getPermissions().clear();
        role.setPermissions(permissions);
        repository.save(role);

        return "Изменения роли внесены в базу данных";
    }
}
