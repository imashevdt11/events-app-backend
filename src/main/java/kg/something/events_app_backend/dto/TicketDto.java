package kg.something.events_app_backend.dto;

import java.util.UUID;

public record TicketDto (
        UUID id,
        Long number,
        Boolean used,
        String eventTitle,
        String userFullName
) {}
