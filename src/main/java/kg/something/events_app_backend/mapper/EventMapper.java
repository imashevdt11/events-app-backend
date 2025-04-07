package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.request.EventRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EventMapper {

    public Event toEntity(EventRequest eventRequest, Image eventImage, User author, Set<Category> categories) {
        return new Event(
                eventRequest.title(),
                eventRequest.description(),
                eventRequest.location(),
                eventRequest.minimumAge(),
                eventRequest.startTime(),
                eventRequest.price(),
                eventRequest.priceCurrency(),
                eventRequest.amountOfPlaces(),
                eventRequest.amountOfPlaces(),
                author,
                eventImage,
                categories
        );
    }
    public EventResponse toEventResponse(Event event, Boolean isLiked, Boolean isDisliked) {
        return new EventResponse(
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getMinimumAge(),
                event.getStartTime(),
                event.getPrice(),
                event.getPriceCurrency(),
                event.getAmountOfPlaces(),
                event.getAmountOfAvailablePlaces(),
                event.getImage().getUrl(),
                event.getCategories(),
                isLiked,
                isDisliked
        );
    }
}
