package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.response.ComplaintListResponse;
import kg.something.events_app_backend.entity.Complaint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintMapper {

    public ComplaintListResponse toComplaintListResponse(Complaint complaint) {
        boolean isEventNull = complaint.getEvent() == null;
        return new ComplaintListResponse(
                complaint.getId(),
                complaint.getText(),
                complaint.getComplaintStatus().name(),
                complaint.getUser().getId(),
                "%s %s".formatted(complaint.getUser().getFirstName(), complaint.getUser().getLastName()),
                isEventNull ? null : complaint.getEvent().getId(),
                isEventNull ? " " : complaint.getEvent().getTitle()
        );
    }
}