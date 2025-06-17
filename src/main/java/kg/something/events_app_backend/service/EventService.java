package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.EventDetailedInAdminPanel;
import kg.something.events_app_backend.dto.EventListDto;
import kg.something.events_app_backend.dto.EventListInAdminPanelDto;
import kg.something.events_app_backend.dto.SalesByEventDto;
import kg.something.events_app_backend.dto.SalesByParticipantDto;
import kg.something.events_app_backend.dto.request.EventUpdateRequest;
import kg.something.events_app_backend.dto.request.PaymentRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.enums.EventGrade;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventService {

    String addComment(UUID eventId, String comment);

    String buyTickets(UUID eventId, PaymentRequest paymentRequest);

    String changeEventImage(UUID eventId, MultipartFile image);

    String changeEventStatus(UUID eventId);

    EventResponse createEvent(String eventRequestString, MultipartFile image);

    String deleteEvent(UUID eventId);

    Event findEventById(UUID id);

    List<EventListDto> getAllEvents();

    List<EventListDto> getAllEventsByUser(UUID userId);

    List<EventListDto> getDislikedEvents();

    EventResponse getEventById(UUID id);

    EventDetailedInAdminPanel getEventDetailedInformationForAdminPanel(UUID id);

    List<EventListInAdminPanelDto> getEventListForAdminPanel();

    List<EventListDto> getEventsByCreationTimePeriod(LocalDate startDate, LocalDate endDate);

    List<EventListDto> getEventsByCategory(String categoryName);

    List<EventListDto> getEventsByStartTimePeriod(LocalDate startDate, LocalDate endDate);

    List<EventListDto> getNotBlockedEventsByUser(UUID userId);

    List<EventListDto> getEventsByUserSubscriptions();

    List<EventListDto> getLikedEvents();

    List<EventListDto> getNotBlockedEvents();

    List<EventListDto> getRelevantEvents();

    List<SalesByEventDto> getSalesStatisticForEvents();

    List<SalesByParticipantDto> getSalesStatisticsByParticipants();

    List<EventListDto> getSavedEvents();

    String removeComment(UUID eventId, UUID commentId);

    String removeEventAsBookmark(UUID eventId);

    String removeRate(UUID eventId, EventGrade rate);

    String saveEventAsBookmark(UUID eventId);

    String rateEvent(UUID eventId, EventGrade rate);

    String updateEvent(UUID eventId, EventUpdateRequest request);
}
