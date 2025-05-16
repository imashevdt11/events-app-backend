package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kg.something.events_app_backend.dto.EventListDto;
import kg.something.events_app_backend.dto.SalesByEventDto;
import kg.something.events_app_backend.dto.SalesByParticipantDto;
import kg.something.events_app_backend.dto.request.EventUpdateRequest;
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
import org.springframework.web.bind.annotation.PutMapping;
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
public class EventControllerApi {

    private final EventService eventService;

    public EventControllerApi(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Оставить комментарий к мероприятию")
    @PostMapping("/comment/{id}")
    public ResponseEntity<String> addComment(@PathVariable("id") UUID eventId,
                                             @RequestParam("comment") String comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addComment(eventId, comment));
    }

    @Operation(summary = "Установка новой фотографии для мероприятия")
    @PostMapping("/change-image/{eventId}")
    public ResponseEntity<?> changeEventImage(@PathVariable("eventId") UUID eventId,
                                              @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(eventService.changeEventImage(eventId, image));
    }

    @Operation(summary = "Создание мероприятия")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestPart("event") String event,
                                                     @RequestPart("image") MultipartFile image) throws InvalidRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event, image));
    }

    @Operation(summary = "Купить билет")
    @PostMapping("/buy-ticket/{eventId}")
    public ResponseEntity<String> butTicket(@PathVariable("eventId") UUID eventId,
                                            @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.buyTickets(eventId, paymentRequest));
    }

    @Operation(summary = "Поставить дизлайк мероприятию")
    @PostMapping("/dislike/{id}")
    public ResponseEntity<String> dislikeEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.rateEvent(eventId, EventGrade.DISLIKE));
    }

    @Operation(summary = "Поставить лайк мероприятию")
    @PostMapping("/like/{id}")
    public ResponseEntity<String> likeEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.rateEvent(eventId, EventGrade.LIKE));
    }

    @Operation(summary = "Получение списка всех мероприятий")
    @GetMapping
    public ResponseEntity<List<EventListDto>> getAllEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
    }

    @Operation(summary = "Получение мероприятий, которым был поставлен 'DISLIKE'")
    @GetMapping("/disliked")
    public ResponseEntity<List<EventListDto>> getDislikedEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getDislikedEvents());
    }

    @Operation(summary = "Получение информации о мероприятии по ID")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventById(id));
    }

    @Operation(summary = "Получение списка мероприятий по категории")
    @GetMapping("/category")
    public ResponseEntity<List<EventListDto>> getEventsByCategory(@RequestParam("category") String category) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByCategory(category));
    }

    @Operation(summary = "Получение списка мероприятий, созданных в указанные период")
    @GetMapping("/creation-time-period")
    public ResponseEntity<List<EventListDto>> getEventsByCreatedTimePeriod(@RequestParam("startDate") LocalDate startDate,
                                                                           @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByCreationTimePeriod(startDate, endDate));
    }

    @Operation(summary = "Получение списка мероприятий, запланированных на указанный период")
    @GetMapping("/start-time-period")
    public ResponseEntity<List<EventListDto>> getEventsByStartTimePeriod(@RequestParam("startDate") LocalDate startDate,
                                                                         @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByStartTimePeriod(startDate, endDate));
    }

    @Operation(summary = "Получение списка мероприятий, созданных указанным пользователем")
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<EventListDto>> getEventsByUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByUser(userId));
    }

    @Operation(summary = "Получение мероприятий, которым был поставлен 'LIKE'")
    @GetMapping("/liked")
    public ResponseEntity<List<EventListDto>> getLikedEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getLikedEvents());
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

    @Operation(summary = "Получение мероприятий из списка 'Сохраненные'")
    @GetMapping("/saved")
    public ResponseEntity<List<EventListDto>> getSavedEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getSavedEvents());
    }

    @Operation(summary = "Добавить мероприятие в 'Сохраненные'")
    @PostMapping("/save-as-bookmark/{id}")
    public ResponseEntity<String> saveEventAsBookmark(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.saveEventAsBookmark(eventId));
    }

    @Operation(summary = "Обновление информации о мероприятии")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable("id") UUID eventId,
                                              @RequestBody @Valid EventUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEvent(eventId, request));
    }

    @Operation(summary = "Удаление мероприятия")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.deleteEvent(eventId));
    }

    @Operation(summary = "Удалить комментарий, оставленный к мероприятию")
    @DeleteMapping("/remove-comment/{id}")
    public ResponseEntity<String> removeComment(@PathVariable("id") UUID eventId,
                                                @RequestParam("commentId") UUID commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeComment(eventId, commentId));
    }

    @Operation(summary = "Убрать, поставленный мероприятию дизлайк")
    @DeleteMapping("/remove-dislike/{id}")
    public ResponseEntity<String> removeDislikeFromEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeRate(eventId, EventGrade.DISLIKE));
    }

    @Operation(summary = "Удалить мероприятие из 'Сохраненных'")
    @DeleteMapping("/remove-as-bookmark/{id}")
    public ResponseEntity<String> removeEventAsBookmark(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeEventAsBookmark(eventId));
    }

    @Operation(summary = "Убрать, поставленный мероприятию лайк")
    @DeleteMapping("/remove-like/{id}")
    public ResponseEntity<String> removeLikeFromEvent(@PathVariable("id") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.removeRate(eventId, EventGrade.LIKE));
    }
}
