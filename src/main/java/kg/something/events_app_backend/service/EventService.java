package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.response.EventResponse;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {

    EventResponse createEvent(String eventRequestString, MultipartFile image);
}
