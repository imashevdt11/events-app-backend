package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.enums.EventGrade;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventService {

    EventResponse createEvent(String eventRequestString, MultipartFile image);

    Event findEventById(UUID id);

    List<EventResponse> getAllEvents();

    EventResponse getEventById(UUID id);

    String rateEvent(UUID eventId, EventGrade rate);

    String removeRate(UUID eventId, EventGrade rate);

    List<EventResponse> getEventsByCategory(String categoryName);

    List<EventResponse> getEventsByCreationTimePeriod(LocalDate startDate, LocalDate endDate);

    List<EventResponse> getEventsByStartTimePeriod(LocalDate startDate, LocalDate endDate);

    String addComment(UUID eventId, String comment);

    String removeComment(UUID eventId, UUID commentId);

    String bookPlace(UUID eventId, Integer numberOfPlaces);
}
