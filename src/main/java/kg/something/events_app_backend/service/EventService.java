package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.response.EventResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EventService {

    EventResponse createEvent(String eventRequestString, MultipartFile image);

    List<EventResponse> getAllEvents();

    EventResponse getEventById(UUID id);
}
