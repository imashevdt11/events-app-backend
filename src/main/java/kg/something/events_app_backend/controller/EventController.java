package kg.something.events_app_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "CREATES EVENT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event has been created"),
            @ApiResponse(responseCode = "400", description = "Required parameter(s) is not present"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Invalid authorization type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @RequestPart("event") String event,
            @RequestPart("image") MultipartFile image)
            throws InvalidRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event, image));
    }
}
