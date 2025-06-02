package kg.something.events_app_backend.mapper;

import kg.something.events_app_backend.dto.CommentDto;
import kg.something.events_app_backend.dto.EventDetailedInAdminPanel;
import kg.something.events_app_backend.dto.EventListInAdminPanelDto;
import kg.something.events_app_backend.dto.EventListDto;
import kg.something.events_app_backend.dto.CategoryDto;
import kg.something.events_app_backend.dto.request.EventRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.service.GradeService;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    private final CommentMapper commentMapper;
    private final GradeService gradeService;

    public EventMapper(CommentMapper commentMapper, GradeService gradeService) {
        this.commentMapper = commentMapper;
        this.gradeService = gradeService;
    }

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

    public EventDetailedInAdminPanel toEventDetailedInAdminPanel(Event event) {
        return new EventDetailedInAdminPanel(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getMinimumAge(),
                event.getStartTime(),
                "%s %s".formatted(event.getPrice(), event.getPriceCurrency()),
                event.getAmountOfPlaces(),
                event.getAmountOfAvailablePlaces(),
                event.getCreatedAt(),
                event.getImage() != null ? event.getImage().getUrl() : " ",
                event.getBlocked(),
                event.getOrganizerUser() != null ?
                        "%s %s".formatted(event.getOrganizerUser().getFirstName(), event.getOrganizerUser().getLastName()) : " ",
                event.getOrganizerUser() != null ?
                        event.getOrganizerUser().getEmail() : " ",
                event.getOrganizerUser() != null ?
                        event.getOrganizerUser().getPhoneNumber() : " "
        );
    }
    public EventListDto toEventListDto(Event event) {
        return new EventListDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getPrice(),
                event.getPriceCurrency(),
                event.getAmountOfAvailablePlaces(),
                event.getAmountOfPlaces() - event.getAmountOfAvailablePlaces(),
                event.getImage().getUrl()
        );
    }

    public EventResponse toEventResponse(Event event, Boolean isLiked, Boolean isDisliked) {
        Set<CategoryDto> categories = event.getCategories().stream()
                .map(c -> new CategoryDto(c.getName()))
                .collect(Collectors.toSet());
        Set<CommentDto> commentDtoSet = event.getEventComments().stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toSet());
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getMinimumAge(),
                event.getStartTime(),
                event.getPrice(),
                event.getPriceCurrency(),
                event.getAmountOfPlaces(),
                event.getAmountOfAvailablePlaces(),
                event.getCreatedAt(),
                event.getImage().getUrl(),
                categories,
                commentDtoSet,
                gradeService.getEventAmountOfLikes(event),
                gradeService.getEventAmountOfDislikes(event),
                isLiked,
                isDisliked
        );
    }

    public EventListInAdminPanelDto toEventListInAdminPanelDto(Event event) {
        return new EventListInAdminPanelDto(
                event.getId(),
                event.getTitle(),
                event.getCreatedAt(),
                event.getBlocked(),
                event.getOrganizerUser() != null ?
                        "%s %s".formatted(event.getOrganizerUser().getFirstName(), event.getOrganizerUser().getLastName()) : " ",
                event.getOrganizerUser() != null ?
                        event.getOrganizerUser().getEmail() : " ",
                event.getOrganizerUser() != null ?
                        event.getOrganizerUser().getPhoneNumber() : " "
        );
    }
}
