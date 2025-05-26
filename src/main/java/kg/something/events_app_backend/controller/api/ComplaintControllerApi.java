package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kg.something.events_app_backend.dto.response.ComplaintResponse;
import kg.something.events_app_backend.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintControllerApi {

    private final ComplaintService service;

    public ComplaintControllerApi(ComplaintService service) {
        this.service = service;
    }

    @Operation(summary = "Отправка жалобы на мероприятие")
    @PostMapping("/{eventId}")
    public ResponseEntity<String> sendComplaint(@PathVariable("eventId") UUID eventId,
                                                @RequestParam("text") String text,
                                                @RequestParam("type") String type) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.sendComplaint(eventId, text, type));
    }

    @Operation(summary = "Получение списка всех жалоб")
    @GetMapping
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllComplaints());
    }

    @Operation(summary = "Изменить статус жалобы")
    @PutMapping("/change-status/{complaintId}")
    public ResponseEntity<String> changeComplaintStatus(@PathVariable UUID complaintId,
                                                        @RequestParam("status") String status) {
        return ResponseEntity.status(HttpStatus.OK).body(service.changeComplaintStatus(complaintId, status));
    }
}
