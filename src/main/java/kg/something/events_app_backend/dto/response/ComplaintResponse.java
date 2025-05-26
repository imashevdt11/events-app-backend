package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kg.something.events_app_backend.enums.ComplaintType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComplaintResponse(
        UUID id,
        @JsonProperty("complaint_text")
        String complaintText,
        @JsonProperty("complaint_type")
        ComplaintType complaintType,
        @JsonProperty("complaint_status")
        String complaintStatus,
        @JsonProperty("created_at")
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonProperty("complaint_author_id")
        UUID complaintAuthorId,
        @JsonProperty("complaint_author_full_name")
        String complaintAuthor,
        @JsonProperty("event_id")
        UUID eventId,
        @JsonProperty("event_title")
        String eventTitle,
        @JsonProperty("event_organizer_full_name")
        String eventOrganizerFullName
) {}
