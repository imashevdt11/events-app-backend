package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.response.ComplaintResponse;
import kg.something.events_app_backend.entity.Complaint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintMapper {

    public ComplaintResponse toComplaintListResponse(Complaint complaint) {
        boolean isEventNull = complaint.getEvent() == null;
        return new ComplaintResponse(
                complaint.getId(),
                complaint.getText(),
                complaint.getType(),
                complaint.getStatus().name(),
                complaint.getCreatedAt(),
                complaint.getUser().getId(),
                "%s %s".formatted(complaint.getUser().getFirstName(), complaint.getUser().getLastName()),
                isEventNull ? null : complaint.getEvent().getId(),
                isEventNull ? " " : complaint.getEvent().getTitle(),
                isEventNull ? " " : "%s %s".formatted(complaint.getEvent().getOrganizerUser().getFirstName(), complaint.getEvent().getOrganizerUser().getLastName())
        );
    }
}