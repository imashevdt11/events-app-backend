package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.dto.RoleListDto;
import kg.something.events_app_backend.dto.response.RoleDetailedResponse;
import kg.something.events_app_backend.dto.response.RoleResponse;
import kg.something.events_app_backend.entity.Permission;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.RoleMapper;
import kg.something.events_app_backend.repository.RoleRepository;
import kg.something.events_app_backend.service.PermissionService;
import kg.something.events_app_backend.service.RoleService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionService permissionService;
    private final UserService userService;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository repository, PermissionService permissionService, @Lazy UserService userService, RoleMapper roleMapper) {
        this.repository = repository;
        this.permissionService = permissionService;
        this.userService = userService;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public String createRole(RoleDto role) {
        User user = userService.getAuthenticatedUser();
        if (repository.existsByName(role.getName())) {
            throw new ResourceAlreadyExistsException("Роль с названием '%s' уже есть в базе данных".formatted(role.getName()));
        }
        Set<Permission> permissions = permissionService.getExistingPermissions(role.getPermissions());
        repository.save(new Role(role.getName(), permissions, user));

        return "Роль создана";
    }

    public String deleteRole(UUID id) {
        Role role = findRoleById(id);
        if (!userService.findUsersByRoleId(role.getId()).isEmpty()) {
            throw new InvalidRequestException("Удаление роли запрещено. Роль привязана к пользователям");
        }
        role.getPermissions().clear();
        repository.delete(role);

        return "Роль удалена";
    }

    public Role findRoleById(UUID id) {
        return Optional.ofNullable(repository.findRoleById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Роль c id '%s' не найдена в базе данных".formatted(id)));
    }

    public Role findRoleByName(String name) {
        return Optional.ofNullable(repository.findRoleByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Роль '%s' не найдена в базе данных".formatted(name)));
    }

    public List<RoleResponse> getAllRoles() {
        return repository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public Integer getAmountOfUsersByRole(UUID roleId) {
        return repository.countUsersByRole(roleId);
    }

    public RoleDetailedResponse getRoleById(UUID id) {
        Role role = findRoleById(id);
        return roleMapper.toRoleDetailedResponse(role);
    }

    public List<RoleListDto> getRolesForList() {
        return repository.findAll().stream()
                .map(role ->
                        new RoleListDto(
                                role.getId(),
                                role.getName(),
                                role.getUser() == null ? "-": "%s %s".formatted(role.getUser().getFirstName(), role.getUser().getLastName()),
                                role.getCreatedAt(),
                                role.getUpdatedAt(),
                                getAmountOfUsersByRole(role.getId()))
                )
                .toList();
    }

    public String updateRole(RoleDto roleDto, UUID id) {
        Role role = findRoleById(id);
        if (repository.existsByName(roleDto.getName()) && !roleDto.getName().equals(role.getName())) {
            throw new ResourceAlreadyExistsException("Роль с названием '%s' уже есть в базе данных".formatted(roleDto.getName()));
        }
        Set<Permission> permissions = permissionService.getExistingPermissions(roleDto.getPermissions());

        role.setName(roleDto.getName());
        role.getPermissions().clear();
        role.setPermissions(permissions);
        repository.save(role);

        return "Изменения роли внесены в базу данных";
    }
}
