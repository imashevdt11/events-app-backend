package kg.something.events_app_backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RoleDetailedResponse(
        UUID id,
        String name,
        String creator,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<PermissionResponse> permissions
) {}
