package kg.something.events_app_backend.repository;

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

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.createdAt BETWEEN :startDate AND :endDate")
    List<Event> findEventsCreatedBetweenDates(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.startTime BETWEEN :startDate AND :endDate")
    List<Event> findEventsWhichStartBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventComments WHERE e.organizerUser = :organizerUser")
    List<Event> findEventsByOrganizerUser(User organizerUser);

    @Query("""
    SELECT new kg.something.events_app_backend.dto.SalesByEventDto(
        e.id,
        e.title,
        COUNT(b.id),
        CASE WHEN e.amountOfPlaces > 0
             THEN CAST(COUNT(b.id) * 100.0 / e.amountOfPlaces AS INTEGER)
             ELSE 0 END,
        CASE WHEN COUNT(b.id) > 0
            THEN SUM(e.price)
            ELSE CAST(0 AS BIGDECIMAL) END)
        FROM Event e
    LEFT JOIN e.eventBookings b
    WHERE e.organizerUser.id = :organizerId
    GROUP BY e.id, e.title, e.amountOfPlaces, e.price
    """)
    List<SalesByEventDto> findSalesByEventForOrganizer(@Param("organizerId") UUID organizerId);

    @Query("""
    SELECT new kg.something.events_app_backend.dto.SalesByParticipantDto(
        u.firstName,
        u.lastName,
        COUNT(b.id),
        SUM(e.price)
    )
    FROM Booking b
    JOIN b.user u
    JOIN b.event e
    WHERE e.organizerUser.id = :organizerId
    GROUP BY u.id, u.firstName, u.lastName
    ORDER BY COUNT(b.id) DESC
    """)
    List<SalesByParticipantDto> findParticipantStatsByOrganizer(@Param("organizerId") UUID organizerId);
}
