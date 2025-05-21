package kg.something.events_app_backend.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kg.something.events_app_backend.dto.TicketDto;
import kg.something.events_app_backend.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketControllerApi {

    private final TicketService service;

    public TicketControllerApi(TicketService service) {
        this.service = service;
    }

    @Operation(summary = "Получение списка купленных пользователем билетов")
    @GetMapping("/purchased-tickets")
    public ResponseEntity<List<TicketDto>> getListOfTicketsPurchasedByUser() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getListOfTicketsPurchasedByUser());
    }

    @Operation(summary = "Получение списка проданных на мероприятие билетов")
    @GetMapping("/sold-tickets/{eventId}")
    public ResponseEntity<List<TicketDto>> getListOfTicketsSoldToEvent(@PathVariable("eventId") UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getListOfTicketsSoldToEvent(eventId));
    }

    @Operation(summary = "Изменить статус билета (использован/не использован)")
    @PutMapping("/{ticketId}")
    public ResponseEntity<String> changeTicketStatus(@PathVariable("ticketId") UUID ticketId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.changeTicketStatus(ticketId));
    }
}
