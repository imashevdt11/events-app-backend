package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.EventListDto;
import kg.something.events_app_backend.dto.SalesByEventDto;
import kg.something.events_app_backend.dto.SalesByParticipantDto;
import kg.something.events_app_backend.dto.request.PaymentRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.enums.EventGrade;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventService {

    String buyTickets(UUID eventId, PaymentRequest paymentRequest);

    EventResponse createEvent(String eventRequestString, MultipartFile image);

    Event findEventById(UUID id);

    List<EventListDto> getAllEvents();

    EventResponse getEventById(UUID id);

    String rateEvent(UUID eventId, EventGrade rate);

    String removeRate(UUID eventId, EventGrade rate);

    List<EventListDto> getEventsByCategory(String categoryName);

    List<EventListDto> getEventsByCreationTimePeriod(LocalDate startDate, LocalDate endDate);

    List<EventListDto> getEventsByStartTimePeriod(LocalDate startDate, LocalDate endDate);

    String addComment(UUID eventId, String comment);

    String removeComment(UUID eventId, UUID commentId);

    List<EventListDto> getEventsByUser(UUID userId);

    String saveEventAsBookmark(UUID eventId);

    String removeEventAsBookmark(UUID eventId);

    List<SalesByEventDto> getSalesStatisticForEvents();

    List<SalesByParticipantDto> getSalesStatisticsByParticipants();
}
