package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Event findEventById(UUID id);

    List<Event> findEventsByCategories(Set<Category> categories);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.createdAt BETWEEN :startDate AND :endDate")
    List<Event> findEventsCreatedBetweenDates(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.startTime BETWEEN :startDate AND :endDate")
    List<Event> findEventsWhichStartBetweenDates(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}
