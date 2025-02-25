package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.request.RoleRegistrationDto;
import kg.something.events_app_backend.entity.Role;

public interface RoleService {

    RoleRegistrationDto createRole(RoleRegistrationDto dto);

    Role findByName(String name);
}
