package kg.something.events_app_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ComplaintListResponse (
        UUID id,
        @JsonProperty("complaint_text")
        String complaintText,
        @JsonProperty("complaint_status")
        String complaintStatus,
        @JsonProperty("complaint_author_id")
        UUID complaintAuthorId,
        @JsonProperty("complaint_author")
        String complaintAuthor,
        @JsonProperty("event_id")
        UUID eventId,
        @JsonProperty("event_title")
        String eventTitle
) {}
