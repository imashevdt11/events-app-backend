package kg.something.events_app_backend.controller.mvc;

import kg.something.events_app_backend.dto.EventListDto;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public String getAllEvents(Model model) {
        try {
            List<EventListDto> events = service.getAllEvents();
            model.addAttribute("events", events);
            return "event_list";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/{id}")
    public String moveToEventDetailsPage(@PathVariable UUID id, Model model) {
        EventResponse event = service.getEventById(id);
        model.addAttribute("event", event);
        return "event_details";
    }
}
