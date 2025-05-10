package kg.something.events_app_backend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDetailedResponse(
        UUID id,
        String name,
        String creator,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
