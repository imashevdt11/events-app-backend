package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.Ticket;
import kg.something.events_app_backend.repository.TicketRepository;
import kg.something.events_app_backend.service.TicketService;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;

    public TicketServiceImpl(TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Ticket ticket) {
        repository.save(ticket);
    }
}
