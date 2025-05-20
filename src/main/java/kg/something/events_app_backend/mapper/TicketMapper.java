package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.TicketDto;
import kg.something.events_app_backend.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketDto toTicketDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getNumber(),
                ticket.getUsed(),
                ticket.getEvent() == null ? " "
                        : ticket.getEvent().getTitle(),
                ticket.getUser() == null ? " "
                        : "%s %s".formatted(ticket.getUser().getFirstName(), ticket.getUser().getLastName())
        );
    }
}
