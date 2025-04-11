package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.SavedEvent;
import kg.something.events_app_backend.entity.User;

public interface SavedEventService {
    SavedEvent findSavedEventByEventAndUser(Event event, User user);

    void save(SavedEvent savedEvent);

    void delete(SavedEvent savedEvent);
}
