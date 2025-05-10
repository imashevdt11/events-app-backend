package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.SavedEvent;
import kg.something.events_app_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedEventRepository extends JpaRepository<SavedEvent, UUID> {
    SavedEvent findSavedEventByEventAndUser(Event event, User user);

    List<SavedEvent> findSavedEventsByUser(User user);
}
