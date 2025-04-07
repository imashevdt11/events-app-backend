package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Event findEventById(UUID id);

    List<Event> findEventsByCategories(Set<Category> categories);
}
