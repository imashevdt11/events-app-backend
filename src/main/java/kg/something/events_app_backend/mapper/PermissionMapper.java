package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.response.PermissionDetailedResponse;
import kg.something.events_app_backend.dto.response.PermissionResponse;
import kg.something.events_app_backend.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionResponse toPermissionResponse(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getUser() == null ? "-" : "%s %s"
                        .formatted(permission.getUser().getFirstName(), permission.getUser().getLastName())
        );
    }

    public PermissionDetailedResponse toPermissionDetailedResponse(Permission permission) {
        return new PermissionDetailedResponse(
                permission.getId(),
                permission.getName(),
                permission.getUser() == null ? "-" : "%s %s"
                        .formatted(permission.getUser().getFirstName(), permission.getUser().getLastName()),
                permission.getCreatedAt(),
                permission.getUpdatedAt()
        );
    }
}
