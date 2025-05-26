package kg.something.events_app_backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "location")
    private String location;

    @PositiveOrZero
    @Column(name = "minimum_age")
    private Integer minimumAge;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @PositiveOrZero
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "price_currency")
    private String priceCurrency;

    @Positive
    @Column(name = "amount_of_places")
    private Integer amountOfPlaces;

    @PositiveOrZero
    @Column(name = "amount_of_available_places")
    private Integer amountOfAvailablePlaces;

    @NotNull
    @Column(name = "blocked")
    private Boolean blocked;

    @ManyToOne
    @JoinColumn(name = "organizer_user_id")
    private User organizerUser;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private Image image;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> eventGrades = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> eventComments = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> eventTickets = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedEvent> savedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> eventComplaints = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "events_categories",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.blocked = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Event() {}

    public Event(
            String title,
            String description,
            String location,
            Integer minimumAge,
            LocalDateTime startTime,
            BigDecimal price,
            String priceCurrency,
            Integer amountOfPlaces,
            Integer amountOfAvailablePlaces,
            User organizerUser,
            Image image,
            Set<Category> categories
    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.minimumAge = minimumAge;
        this.startTime = startTime;
        this.price = price;
        this.priceCurrency = priceCurrency;
        this.amountOfPlaces = amountOfPlaces;
        this.amountOfAvailablePlaces = amountOfAvailablePlaces;
        this.organizerUser = organizerUser;
        this.image = image;
        this.categories = categories;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Integer getAmountOfAvailablePlaces() {
        return amountOfAvailablePlaces;
    }

    public void setAmountOfAvailablePlaces(Integer amountOfAvailablePlaces) {
        this.amountOfAvailablePlaces = amountOfAvailablePlaces;
    }

    public Integer getAmountOfPlaces() {
        return amountOfPlaces;
    }

    public void setAmountOfPlaces(Integer amountOfPlaces) {
        this.amountOfPlaces = amountOfPlaces;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public List<Ticket> getEventTickets() {
        return eventTickets;
    }

    public void setEventTickets(List<Ticket> eventTickets) {
        this.eventTickets = eventTickets;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public User getOrganizerUser() {
        return organizerUser;
    }

    public void setOrganizerUser(User organizerUser) {
        this.organizerUser = organizerUser;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Ticket> getEventBookings() {
        return eventTickets;
    }

    public void setEventBookings(List<Ticket> eventTickets) {
        this.eventTickets = eventTickets;
    }

    public List<Comment> getEventComments() {
        return eventComments;
    }

    public void setEventComments(List<Comment> eventComments) {
        this.eventComments = eventComments;
    }

    public List<Complaint> getEventComplaints() {
        return eventComplaints;
    }

    public void setEventComplaints(List<Complaint> eventComplaints) {
        this.eventComplaints = eventComplaints;
    }

    public List<Grade> getEventGrades() {
        return eventGrades;
    }

    public void setEventGrades(List<Grade> eventGrades) {
        this.eventGrades = eventGrades;
    }

    public List<SavedEvent> getSavedByUsers() {
        return savedByUsers;
    }

    public void setSavedByUsers(List<SavedEvent> savedByUsers) {
        this.savedByUsers = savedByUsers;
    }
}
