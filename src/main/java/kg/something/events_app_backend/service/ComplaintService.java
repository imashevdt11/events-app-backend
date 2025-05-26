package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.response.ComplaintResponse;

import java.util.List;
import java.util.UUID;

public interface ComplaintService {

    String changeComplaintStatus(UUID complaintId, String status);

    List<ComplaintResponse> getAllComplaints();

    List<String> getAllComplaintsStatuses();

    ComplaintResponse getComplaintById(UUID id);

    String sendComplaint(UUID eventId, String text, String type);
}
