package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Ticket;
import kg.something.events_app_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findTicketsByEvent(Event event);

    List<Ticket> findTicketsByUser(User user);

    @Query(value = "SELECT MAX(t.number) FROM tickets t WHERE t.event_id = :eventId", nativeQuery = true)
    Long getMaxTicketNumberByEvent(@Param("eventId") UUID eventId);
}