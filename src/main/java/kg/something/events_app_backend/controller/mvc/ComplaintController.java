package kg.something.events_app_backend.controller.mvc;

import kg.something.events_app_backend.dto.EventDetailedInAdminPanel;
import kg.something.events_app_backend.dto.response.ComplaintResponse;
import kg.something.events_app_backend.service.ComplaintService;
import kg.something.events_app_backend.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final EventService eventService;

    public ComplaintController(ComplaintService complaintService, EventService eventService) {
        this.complaintService = complaintService;
        this.eventService = eventService;
    }

    @GetMapping("/change-status/{id}")
    public String changeUserRole(@PathVariable UUID id,
                                 @RequestParam("status") String status,
                                 Model model) {
        try {
            complaintService.changeComplaintStatus(id, status);
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось поменять статус жалобы по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
        return "redirect:/complaints";
    }

    @GetMapping
    public String getAllComplaints(Model model) {
        try {
            List<ComplaintResponse> complaints = complaintService.getAllComplaints();
            List<String> complaintsStatuses = complaintService.getAllComplaintsStatuses();
            model.addAttribute("complaints", complaints);
            model.addAttribute("complaints_statuses", complaintsStatuses);
            return "complaint_list";
        } catch (Exception e) {
            model.addAttribute("exception", "Не удалось получить список жалоб по причине: %s".formatted(e.getLocalizedMessage()));
            return "error";
        }
    }

    @GetMapping("/detailed/{id}")
    public String moveToDetailedPage(@PathVariable UUID id, Model model) {
        ComplaintResponse complaint = complaintService.getComplaintById(id);
        EventDetailedInAdminPanel event = eventService.getEventDetailedInformationForAdminPanel(complaint.eventId());
        model.addAttribute("complaint", complaint);
        model.addAttribute("event", event);
        return "complaint_details";
    }
}
