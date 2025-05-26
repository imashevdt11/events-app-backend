package kg.something.events_app_backend.controller.mvc;

import kg.something.events_app_backend.dto.response.ComplaintListResponse;
import kg.something.events_app_backend.service.ComplaintService;
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

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @GetMapping("/change-status/{id}")
    public String changeUserRole(@PathVariable UUID id,
                                 @RequestParam("status") String status) {
        try {
            service.changeComplaintStatus(id, status);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/complaints";
    }

    @GetMapping
    public String getAllComplaints(Model model) {
        try {
            List<ComplaintListResponse> complaints = service.getAllComplaints();
            List<String> complaintsStatuses = service.getAllComplaintsStatuses();
            model.addAttribute("complaints", complaints);
            model.addAttribute("complaints_statuses", complaintsStatuses);
            return "complaint_list";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/detailed/{id}")
    public String moveToDetailedPage(@PathVariable UUID id, Model model) {
        ComplaintListResponse complaint = service.getComplaintById(id);
        model.addAttribute("complaint", complaint);
        return "complaint_details";
    }
}
