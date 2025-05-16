package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.SavedEvent;
import kg.something.events_app_backend.entity.User;

import java.util.List;

public interface SavedEventService {

    void delete(SavedEvent savedEvent);

    SavedEvent findSavedEventByEventAndUser(Event event, User user);

    List<SavedEvent> findSavedEventsByUser(User user);

    void save(SavedEvent savedEvent);
}
