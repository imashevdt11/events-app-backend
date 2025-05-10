package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.SavedEvent;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.repository.SavedEventRepository;
import kg.something.events_app_backend.service.SavedEventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedEventServiceImpl implements SavedEventService {

    private final SavedEventRepository repository;

    public SavedEventServiceImpl(SavedEventRepository repository) {
        this.repository = repository;
    }

    public SavedEvent findSavedEventByEventAndUser(Event event, User user) {
        return repository.findSavedEventByEventAndUser(event, user);
    }

    public List<SavedEvent> findSavedEventsByUser(User user) {
        return repository.findSavedEventsByUser(user);
    }

    public void save(SavedEvent savedEvent) {
        repository.save(savedEvent);
    }

    public void delete(SavedEvent savedEvent) {
        repository.delete(savedEvent);
    }
}
