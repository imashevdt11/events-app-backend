package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.TicketDto;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Ticket;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.TicketMapper;
import kg.something.events_app_backend.repository.TicketRepository;
import kg.something.events_app_backend.service.EventService;
import kg.something.events_app_backend.service.TicketService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;
    private final TicketMapper ticketMapper;
    private final UserService userService;
    private final EventService eventService;

    public TicketServiceImpl(TicketRepository repository, TicketMapper ticketMapper, UserService userService, @Lazy EventService eventService) {
        this.repository = repository;
        this.ticketMapper = ticketMapper;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public String changeTicketStatus(UUID ticketId) {
        User user = userService.getAuthenticatedUser();
        Ticket ticket = repository.findTicketById(ticketId);
        if (ticket == null) {
            throw new ResourceNotFoundException("Билет с id '%s' не найден".formatted(ticketId));
        }
        Event event = eventService.findEventById(ticket.getEvent().getId());
        if (!event.getOrganizerUser().getId().equals(user.getId())) {
            throw new InvalidRequestException("Пользователь не может менять статус билетов не на свое мероприятие");
        }
        boolean updatedStatus = !ticket.getUsed();
        ticket.setUsed(updatedStatus);
        save(ticket);

        return updatedStatus ? "Билет переведен в статус 'Использован'" : "Билет переведен в статус 'Не использован'";
    }

    @Override
    public List<TicketDto> getListOfTicketsPurchasedByUser() {
        User user = userService.getAuthenticatedUser();

        return getTicketsByUser(user);
    }

    @Override
    public List<TicketDto> getListOfTicketsSoldToEvent(UUID eventId) {
        User user = userService.getAuthenticatedUser();
        Event event = eventService.findEventById(eventId);
        if (!user.getId().equals(event.getOrganizerUser().getId())) {
            throw new InvalidRequestException("Пользователь не может посмотреть информацию о проданных билетах по созданному другим пользователем мероприятию");
        }

        return getTicketsByEvent(event);
    }

    @Override
    public Long getMaxTicketNumberByEvent(Event event) {
        return repository.getMaxTicketNumberByEvent(event.getId());
    }

    @Override
    public List<TicketDto> getTicketsByEvent(Event event) {
        return repository.findTicketsByEvent(event).stream()
                .map(ticketMapper::toTicketDto)
                .toList();
    }

    @Override
    public TicketDto getTicketByEventIdAndNumber(UUID eventId, Long ticketNumber) {
        userService.getAuthenticatedUser();
        Event event = eventService.findEventById(eventId);

        return ticketMapper.toTicketDto(repository.findTicketByEventAndNumber(event, ticketNumber));
    }

    @Override
    public List<TicketDto> getTicketsByUser(User user) {
        return repository.findTicketsByUser(user).stream()
                .map(ticketMapper::toTicketDto)
                .toList();
    }

    @Override
    public void save(Ticket ticket) {
        repository.save(ticket);
    }
}
