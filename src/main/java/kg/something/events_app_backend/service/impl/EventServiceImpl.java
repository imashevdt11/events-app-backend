package kg.something.events_app_backend.service.impl;

import com.cloudinary.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kg.something.events_app_backend.dto.request.EventRequest;
import kg.something.events_app_backend.dto.response.EventResponse;
import kg.something.events_app_backend.entity.Booking;
import kg.something.events_app_backend.entity.Category;
import kg.something.events_app_backend.entity.Comment;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.Grade;
import kg.something.events_app_backend.entity.SavedEvent;
import kg.something.events_app_backend.entity.Image;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.enums.EventGrade;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.EventMapper;
import kg.something.events_app_backend.repository.EventRepository;
import kg.something.events_app_backend.service.BookingService;
import kg.something.events_app_backend.service.CategoryService;
import kg.something.events_app_backend.service.CloudinaryService;
import kg.something.events_app_backend.service.CommentService;
import kg.something.events_app_backend.service.EventService;
import kg.something.events_app_backend.service.GradeService;
import kg.something.events_app_backend.service.SavedEventService;
import kg.something.events_app_backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final EventMapper eventMapper;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final Validator validator;
    private final GradeService gradeService;
    private final CommentService commentService;
    private final BookingService bookingService;
    private final SavedEventService savedEventService;

    public EventServiceImpl(CategoryService categoryService, CloudinaryService cloudinaryService, EventRepository repository, EventMapper eventMapper, ObjectMapper objectMapper, UserService userService, Validator validator, GradeService gradeService, CommentService commentService, BookingService bookingService, SavedEventService savedEventService) {
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.repository = repository;
        this.eventMapper = eventMapper;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.validator = validator;
        this.gradeService = gradeService;
        this.commentService = commentService;
        this.bookingService = bookingService;
        this.savedEventService = savedEventService;
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
        Event event = eventMapper.toEntity(eventRequest, savedImage, authenticatedUser, categories);
        repository.save(event);

        return eventMapper.toEventResponse(event, null, null);
    }

    public List<EventResponse> getAllEvents() {
        List<Event> events = repository.findAll();
        return events.stream().map(
                event -> eventMapper.toEventResponse(event, null, null))
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(UUID id) {
        Event event = findEventById(id);
        Boolean isLiked = null;
        Boolean isDisliked = null;
        if (userService.isAuthenticated()) {
            Grade grade = gradeService.findGradeByEventAndUser(event, userService.getAuthenticatedUser());
            isLiked = grade.getName().equals(EventGrade.LIKE);
            isDisliked = grade.getName().equals(EventGrade.DISLIKE);
        }
        return eventMapper.toEventResponse(event, isLiked, isDisliked);
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

    public Event findEventById(UUID id) {
        return Optional.ofNullable(repository.findEventById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Мероприятие с id '%s' не найдено в базе данных".formatted(id)));
    }

    @Transactional
    public String rateEvent(UUID eventId, EventGrade rate) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);

        Grade grade = gradeService.findGradeByEventAndUser(event, user);
        if (grade == null) {
            gradeService.save(new Grade(rate, event, user));
            return "Пользователь '%s %s' поставил оценку '%s' мероприятию '%s'"
                    .formatted(
                            user.getFirstName(),
                            user.getLastName(),
                            rate.name(),
                            event.getTitle()
                    );
        } else {
            if (grade.getName().equals(EventGrade.DISLIKE) && rate.equals(EventGrade.LIKE)) {
                throw new InvalidRequestException("Аутентифицированный пользователь не может поставить лайк мероприятию, потому что поставил дизлайк");
            } else if (grade.getName().equals(EventGrade.LIKE) && rate.equals(EventGrade.DISLIKE)) {
                throw new InvalidRequestException("Аутентифицированный пользователь не может поставить дизлайк мероприятию, потому что поставил лайк");
            } else {
                throw new InvalidRequestException("Аутентифицированный пользователь уже поставил '%s' мероприятию".formatted(grade.getName()));
            }
        }
    }

    @Transactional
    public String removeRate(UUID eventId, EventGrade rate) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);

        Grade grade = gradeService.findGradeByEventAndUser(event, user);
        if (grade == null) {
            throw new InvalidRequestException("Аутентифицированный пользователь не ставил оценку данному мероприятию");
        } else {
            if (grade.getName().equals(EventGrade.DISLIKE) && rate.equals(EventGrade.LIKE)) {
                throw new InvalidRequestException("Аутентифицированный пользователь не ставил лайк данному мероприятию");
            } else if (grade.getName().equals(EventGrade.LIKE) && rate.equals(EventGrade.DISLIKE)) {
                throw new InvalidRequestException("Аутентифицированный пользователь не ставил дизлайк данному мероприятию");
            }
            gradeService.delete(grade);
            return "Оценка '%s' от пользователя '%s %s' удалена с мероприятия '%s'"
                    .formatted(
                            rate.name(),
                            user.getFirstName(),
                            user.getLastName(),
                            event.getTitle()
                    );
        }
    }

    public List<EventResponse> getEventsByCategory(String categoryName) {
        Category category = categoryService.findCategoryByName(categoryName);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        List<Event> events = repository.findEventsByCategories(categories);
        return events.stream().map(
                event -> eventMapper.toEventResponse(event, null, null))
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByCreationTimePeriod(LocalDate startDate, LocalDate endDate) {
        List<Event> events = repository.findEventsCreatedBetweenDates(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return events.stream()
                .map(event -> eventMapper.toEventResponse(event, null, null))
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByStartTimePeriod(LocalDate startDate, LocalDate endDate) {
        List<Event> events = repository.findEventsWhichStartBetweenDates(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return events.stream()
                .map(event -> eventMapper.toEventResponse(event, null, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public String addComment(UUID eventId, String commentText) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);

        commentService.save(new Comment(commentText, event, user));
        return "Пользователь '%s %s' оставил комментарий к мероприятию '%s'"
                .formatted(
                        user.getFirstName(),
                        user.getLastName(),
                        event.getTitle()
                );
    }

    @Transactional
    public String removeComment(UUID eventId, UUID commentId) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);
        Comment comment = commentService.findCommentById(commentId);

        if (user.getId().equals(comment.getUser().getId())) {
            commentService.delete(comment);
            return "Пользователь '%s %s' удалил комментарий к мероприятию '%s'"
                    .formatted(
                            user.getFirstName(),
                            user.getLastName(),
                            event.getTitle()
                    );
        } else {
            throw new InvalidRequestException("Пользователь '%s %s' не является автором указанного мероприятия"
                    .formatted(
                            user.getFirstName(),
                            user.getLastName()
                    ));
        }
    }

    @Transactional
    public String bookPlace(UUID eventId, Integer numberOfPlaces) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);

        if (event.getAmountOfAvailablePlaces() < numberOfPlaces) {
            throw new InvalidRequestException("Количество запрошенных мест превышает доступное. Доступное количество мест на мероприятие: %s".formatted(event.getAmountOfAvailablePlaces()));
        }
        if (event.getOrganizerUser().getId().equals(user.getId())) {
            throw new InvalidRequestException("Организатор не может бронировать места на свои же мероприятия");
        }
        for (int i = 0; i < numberOfPlaces; i++) {
            bookingService.save(new Booking(event, user));
        }
        event.setAmountOfAvailablePlaces(event.getAmountOfAvailablePlaces() - numberOfPlaces);
        return "%s мест(о) забронированы(о) на мероприятии '%s' пользователем '%s %s'"
                .formatted(
                        numberOfPlaces,
                        event.getTitle(),
                        user.getFirstName(),
                        user.getLastName()
                );
    }

    public List<EventResponse> getEventsByUser(UUID userId) {
        User user = userService.findUserById(userId);
        return repository.findEventsByOrganizerUser(user)
                .stream()
                .map(event -> eventMapper.toEventResponse(event, null, null))
                .toList();
    }

    public String saveEventAsBookmark(UUID eventId) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);

        SavedEvent savedEvent = savedEventService.findSavedEventByEventAndUser(event, user);
        if (savedEvent == null) {
            savedEventService.save(new SavedEvent(event, user));
            return "Пользователь '%s %s' добавил мероприятие '%s' в 'Избранное'"
                    .formatted(
                            user.getFirstName(),
                            user.getLastName(),
                            event.getTitle()
                    );
        } else {
            throw new InvalidRequestException("Аутентифицированный пользователь уже добавил мероприятие в избранное");
        }
    }

    public String removeEventAsBookmark(UUID eventId) {
        User user = userService.getAuthenticatedUser();
        Event event = findEventById(eventId);

        SavedEvent savedEvent = savedEventService.findSavedEventByEventAndUser(event, user);
        if (savedEvent == null) {
            throw new ResourceNotFoundException("Указанное мероприятие не сохранялось пользователем");
        } else {
            savedEventService.delete(savedEvent);
            return "Мероприятие удаленно из 'Избранного'";
        }
    }
}
