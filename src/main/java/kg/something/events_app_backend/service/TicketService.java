package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.TicketDto;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Ticket;
import kg.something.events_app_backend.entity.User;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    String changeTicketStatus(UUID ticketId);

    Long getMaxTicketNumberByEvent(Event event);

    List<TicketDto> getTicketsByEvent(Event event);

    List<TicketDto> getTicketsByUser(User user);

    List<TicketDto> getListOfTicketsPurchasedByUser();

    List<TicketDto> getListOfTicketsSoldToEvent(UUID eventId);

    TicketDto getTicketByEventIdAndNumber(UUID eventId, Long ticketNumber);

    void save(Ticket ticket);
}
