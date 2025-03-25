package kg.something.events_app_backend.service.impl;

import com.cloudinary.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kg.something.events_app_backend.dto.request.EventRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.repository.EventRepository;
import kg.something.events_app_backend.service.CategoryService;
import kg.something.events_app_backend.service.CloudinaryService;
import kg.something.events_app_backend.service.EventService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final Validator validator;

    public EventServiceImpl(CategoryService categoryService, CloudinaryService cloudinaryService, EventRepository eventRepository, ObjectMapper objectMapper, UserService userService, Validator validator) {
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.validator = validator;
    }

    @Transactional
    public EventResponse createEvent(String eventRequestString, MultipartFile image) {

        User authenticatedUser = userService.getAuthenticatedUser();
        Image savedImage = cloudinaryService.saveImage(image);

        EventRequest eventRequest = parseAndValidateEvent(eventRequestString);

        Set<Category> categories = new HashSet<>();
        for (Category category : eventRequest.categories()) {
            Category existingCategory = categoryService.findCategoryByName(category.getName());
            if (existingCategory == null) {
                throw new ResourceNotFoundException("Категория с названием '" + category.getName() + "' не найдена");
            }
            categories.add(existingCategory);
        }

        Event event = new Event(
                eventRequest.title(),
                eventRequest.description(),
                eventRequest.location(),
                eventRequest.minimumAge(),
                eventRequest.startTime(),
                eventRequest.price(),
                eventRequest.priceCurrency(),
                eventRequest.amountOfPlaces(),
                eventRequest.amountOfPlaces(),
                authenticatedUser,
                savedImage,
                categories
        );

        eventRepository.save(event);

        return new EventResponse(
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getMinimumAge(),
                event.getStartTime(),
                event.getPrice(),
                eventRequest.priceCurrency(),
                event.getAmountOfPlaces(),
                event.getAmountOfAvailablePlaces(),
                savedImage.getUrl(),
                categories
        );
    }

    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> new EventResponse(
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
                event.getCategories()
        )).collect(Collectors.toList());
    }

    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findEventById(id);
        if (event == null) {
            throw new ResourceNotFoundException("User not found with id: %s".formatted(id));
        }
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
                event.getCategories()
        );
    }

    private EventRequest parseAndValidateEvent(String eventRequestString) {
        try {
            EventRequest eventRequest = objectMapper.readValue(eventRequestString, EventRequest.class);
            BindingResult bindingResult = new BeanPropertyBindingResult(eventRequest, "eventRequest");
            validator.validate(eventRequestString, bindingResult);

            if (StringUtils.isBlank(eventRequest.title())) {
                throw new InvalidRequestException("Title cannot be empty");
            } else if (eventRequest.title().length() < 5 || eventRequest.title().length() > 50) {
                throw new InvalidRequestException("Title size must be between 5 and 50");
            }

            if (StringUtils.isBlank(eventRequest.description())) {
                throw new InvalidRequestException("Description cannot be empty");
            }

            if (StringUtils.isBlank(eventRequest.location())) {
                throw new InvalidRequestException("Location cannot be empty");
            } else if (eventRequest.location().length() < 20 || eventRequest.location().length() > 100) {
                throw new InvalidRequestException("Location size must be between 20 and 100");
            }

            if (eventRequest.minimumAge() != null && eventRequest.minimumAge() < 0) {
                throw new InvalidRequestException("Minimum age must be zero or positive");
            }

            if (eventRequest.startTime() == null) {
                throw new InvalidRequestException("Start time cannot be null");
            } else if (eventRequest.startTime().isBefore(LocalDateTime.now())) {
                throw new InvalidRequestException("Start time must be in the future");
            }

            if (eventRequest.price() != null && eventRequest.price().compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidRequestException("Price must be zero or positive");
            }

            if (StringUtils.isBlank(eventRequest.priceCurrency())) {
                throw new InvalidRequestException("Price currency cannot be empty");
            }

            if (eventRequest.amountOfPlaces() == null || eventRequest.amountOfPlaces() <= 0) {
                throw new InvalidRequestException("Amount of places must be greater than zero");
            }
            return eventRequest;
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

}
