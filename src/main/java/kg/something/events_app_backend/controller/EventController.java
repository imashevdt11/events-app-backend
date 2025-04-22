package kg.something.events_app_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kg.something.events_app_backend.dto.EventListDto;
import kg.something.events_app_backend.dto.SalesByEventDto;
import kg.something.events_app_backend.dto.SalesByParticipantDto;
import kg.something.events_app_backend.dto.request.PaymentRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.enums.EventGrade;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "CREATE EVENT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event has been created"),
            @ApiResponse(responseCode = "400", description = "Required parameter(s) is not present"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Invalid authorization type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestPart("event") String event,
                                                     @RequestPart("image") MultipartFile image) throws InvalidRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event, image));
    }

    @Operation(summary = "GET ALL EVENTS")
    @GetMapping
    public ResponseEntity<List<EventListDto>> getAllEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
    }

    @Operation(summary = "GET EVENTS BY CATEGORY")
    @GetMapping("/category")
    public ResponseEntity<List<EventListDto>> getEventsByCategory(@RequestParam("category") String category) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByCategory(category));
    }

    @Operation(summary = "GET EVENTS CREATED IN SPECIFIED PERIOD")
    @GetMapping("/creation-time-period")
    public ResponseEntity<List<EventListDto>> getEventsByCreatedTimePeriod(@RequestParam("startDate") LocalDate startDate,
                                                                           @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByCreationTimePeriod(startDate, endDate));
    }

    @Operation(summary = "GET EVENTS THAT WILL BE STARTED IN SPECIFIED PERIOD")
    @GetMapping("/start-time-period")
    public ResponseEntity<List<EventListDto>> getEventsByStartTimePeriod(@RequestParam("startDate") LocalDate startDate,
                                                                         @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByStartTimePeriod(startDate, endDate));
    }

    @Operation(summary = "GET EVENT INFORMATION BY ID")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventById(id));
    }

    @Operation(summary = "RATE (LIKE) EVENT")
    @PostMapping("/like/{id}")
    public ResponseEntity<String> likeEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.rateEvent(eventId, EventGrade.LIKE));
    }

    @Operation(summary = "REMOVE RATE (LIKE) FROM EVENT")
    @DeleteMapping("/remove-like/{id}")
    public ResponseEntity<String> removeLikeFromEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.removeRate(eventId, EventGrade.LIKE));
    }

    @Operation(summary = "RATE (DISLIKE) EVENT")
    @PostMapping("/dislike/{id}")
    public ResponseEntity<String> dislikeEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.rateEvent(eventId, EventGrade.DISLIKE));
    }

    @Operation(summary = "REMOVE RATE (DISLIKE) FROM EVENT")
    @DeleteMapping("/remove-dislike/{id}")
    public ResponseEntity<String> removeDislikeFromEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeRate(eventId, EventGrade.DISLIKE));
    }

    @Operation(summary = "ADD COMMENT TO EVENT")
    @PostMapping("/comment/{id}")
    public ResponseEntity<String> addComment(@PathVariable("id") UUID eventId,
                                             @RequestParam("comment") String comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addComment(eventId, comment));
    }

    @Operation(summary = "REMOVE COMMENT FROM EVENT")
    @DeleteMapping("/remove-comment/{id}")
    public ResponseEntity<String> removeComment(@PathVariable("id") UUID eventId,
                                                @RequestParam("commentId") UUID commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeComment(eventId, commentId));
    }

    @Operation(summary = "Купить билет")
    @PostMapping("/buy-ticket/{eventId}")
    public ResponseEntity<String> bookPlace(@PathVariable("eventId") UUID eventId,
                                            @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.buyTickets(eventId, paymentRequest));
    }

    @Operation(summary = "GET EVENTS CREATED BY USER")
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<EventListDto>> getEventsByUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByUser(userId));
    }

    @Operation(summary = "SAVE EVENT AS BOOKMARK")
    @PostMapping("/save-as-bookmark/{id}")
    public ResponseEntity<String> saveEventAsBookmark(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.saveEventAsBookmark(eventId));
    }

    @Operation(summary = "REMOVE EVENT AS BOOKMARK")
    @DeleteMapping("/remove-as-bookmark/{id}")
    public ResponseEntity<String> removeEventAsBookmark(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeEventAsBookmark(eventId));
    }

    @Operation(summary = "Статистика по продажам билетов на мероприятия, созданные аутентифицированным пользователем")
    @GetMapping("/stats/sales-for-events")
    public ResponseEntity<List<SalesByEventDto>> getSalesStatisticForEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getSalesStatisticForEvents());
    }

    @Operation(summary = "Статистика по количеству билетов и сумме, потраченной на них, для каждого участника")
    @GetMapping("/stats/sales-by-participants")
    public ResponseEntity<List<SalesByParticipantDto>> getSalesStatisticsByParticipants() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getSalesStatisticsByParticipants());
    }
}
