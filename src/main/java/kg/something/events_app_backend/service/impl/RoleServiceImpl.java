package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.request.RoleRegistrationDto;
import kg.something.events_app_backend.entity.Role;
import kg.something.events_app_backend.repository.RoleRepository;
import kg.something.events_app_backend.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    public RoleRegistrationDto createRole(RoleRegistrationDto dto) {
        Role role = new Role(dto.name());

        repository.save(role);

        return new RoleRegistrationDto(dto.name());
    }

    public Role findByName(String name) {
        return repository.findByName(name);
    }
}
