package kg.something.events_app_backend.service;

import kg.something.events_app_backend.dto.TicketDto;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Ticket;
import kg.something.events_app_backend.entity.User;

import java.util.List;

public interface TicketService {

//    List<TicketDto> getTicketsByEventAndUser(Event event, User user);

    Long getMaxTicketNumberByEvent(Event event);

    List<TicketDto> getTicketsByEvent(Event event);

    List<TicketDto> getTicketsByUser(User user);

    void save(Ticket ticket);
}
