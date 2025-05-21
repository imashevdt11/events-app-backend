package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.response.ComplaintListResponse;

import java.util.List;
import java.util.UUID;

public interface ComplaintService {

    String changeComplaintStatus(UUID complaintId, String status);

    List<ComplaintListResponse> getAllComplaints();

    String sendComplaint(UUID eventId, String text);
}
