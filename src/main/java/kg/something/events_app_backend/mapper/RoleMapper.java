package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.response.RoleDetailedResponse;
import kg.something.events_app_backend.dto.response.RoleResponse;
import kg.something.events_app_backend.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponse toRoleResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getUser() == null ? "-" : "%s %s"
                        .formatted(role.getUser().getFirstName(), role.getUser().getLastName())
        );
    }

    public RoleDetailedResponse toRoleDetailedResponse(Role role) {
        return new RoleDetailedResponse(
                role.getId(),
                role.getName(),
                role.getUser() == null ? "-" : "%s %s"
                        .formatted(role.getUser().getFirstName(), role.getUser().getLastName()),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}
