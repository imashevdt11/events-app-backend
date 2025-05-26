package kg.something.events_app_backend.repository;

import jakarta.validation.constraints.NotNull;
import kg.something.events_app_backend.dto.SalesByEventDto;
import kg.something.events_app_backend.dto.SalesByParticipantDto;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.User;
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

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.organizerUser = :organizerUser")
    List<Event> findEventsByOrganizerUser(User organizerUser);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.createdAt BETWEEN :startDate AND :endDate")
    List<Event> findEventsCreatedBetweenDates(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.startTime BETWEEN :startDate AND :endDate")
    List<Event> findEventsWhichStartBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT new kg.something.events_app_backend.dto.SalesByParticipantDto(
        u.firstName,
        u.lastName,
        COUNT(t.id),
        SUM(e.price)
    )
    FROM Ticket t
    JOIN t.user u
    JOIN t.event e
    WHERE e.organizerUser.id = :organizerId
    GROUP BY u.id, u.firstName, u.lastName
    ORDER BY COUNT(t.id) DESC
    """)
    List<SalesByParticipantDto> findParticipantStatsByOrganizer(@Param("organizerId") UUID organizerId);

    @Query("""
    SELECT new kg.something.events_app_backend.dto.SalesByEventDto(
        e.id,
        e.title,
        COUNT(t.id),
        CASE WHEN e.amountOfPlaces > 0
             THEN CAST(COUNT(t.id) * 100.0 / e.amountOfPlaces AS INTEGER)
             ELSE 0 END,
        CASE WHEN COUNT(t.id) > 0
            THEN SUM(e.price)
            ELSE CAST(0 AS BIGDECIMAL) END)
        FROM Event e
    LEFT JOIN e.eventTickets t
    WHERE e.organizerUser.id = :organizerId
    GROUP BY e.id, e.title, e.amountOfPlaces, e.price
    """)
    List<SalesByEventDto> findSalesByEventForOrganizer(@Param("organizerId") UUID organizerId);

    List<Event> findEventsByStartTimeAfter(@NotNull LocalDateTime startTimeAfter);
}
