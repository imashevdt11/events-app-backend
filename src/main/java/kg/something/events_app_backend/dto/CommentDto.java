package kg.something.events_app_backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDto(
        UUID authorId,
        String authorImage,
        String authorFirstName,
        String authorLastName,
        UUID commentId,
        String comment,
        LocalDateTime commentDate
) {}
