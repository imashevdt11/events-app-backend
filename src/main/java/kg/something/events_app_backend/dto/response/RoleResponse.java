package kg.something.events_app_backend.dto.response;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name,
        String creator
) {}
