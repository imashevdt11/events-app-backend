package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.RoleDto;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.exception.ResourceAlreadyExistsException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.RoleRepository;
import kg.something.events_app_backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    public String createRole(RoleDto role) {
        if (repository.existsByName(role.getName())) {
            throw new ResourceAlreadyExistsException("Роль с названием '" + role.getName() + "' уже есть в базе данных");
        }
        repository.save(new Role(role.getName()));

        return "Роль '" + role.getName() + "' сохранена";
    }

    public String deleteRole(UUID id) {
        Role role = repository.findRoleById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Роль с id " + id + " не найдена");
        }
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

        String oldRoleName = role.getName();
        if (role.getName().equals(roleDto.getName())) {
            return "Роль не изменена. Данные из запроса и записи в базе данных идентичны";
        }
        if (repository.existsByName(roleDto.getName())) {
            throw new ResourceAlreadyExistsException("Роль с названием '" + role.getName() + "' уже есть в базе данных");
        }
        role.setName(roleDto.getName());
        repository.save(role);

        return "Название категории изменено с '" + oldRoleName + "' на '" + role.getName() + "'";
    }
}
