package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.TicketDto;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Ticket;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.mapper.TicketMapper;
import kg.something.events_app_backend.repository.TicketRepository;
import kg.something.events_app_backend.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;
    private final TicketMapper ticketMapper;

    public TicketServiceImpl(TicketRepository repository, TicketMapper ticketMapper) {
        this.repository = repository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    public List<TicketDto> getTicketsByEvent(Event event) {
        return repository.findTicketsByEvent(event).stream()
                .map(ticketMapper::toTicketDto)
                .toList();

    }

    @Override
    public List<TicketDto> getTicketsByUser(User user) {
        return repository.findTicketsByUser(user).stream()
                .map(ticketMapper::toTicketDto)
                .toList();

    }

    @Override
    public Long getMaxTicketNumberByEvent(Event event) {
        return repository.getMaxTicketNumberByEvent(event.getId());

    }
    @Override
    public void save(Ticket ticket) {
        repository.save(ticket);
    }
}
