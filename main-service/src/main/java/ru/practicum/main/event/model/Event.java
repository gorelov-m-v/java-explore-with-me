package ru.practicum.main.event.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.constants.Pattern;
import ru.practicum.main.event.model.enums.EventState;
import ru.practicum.main.user.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    @Transient
    private final String datePattern = Pattern.DATE;
    @Transient
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    private int confirmedRequests;
    @Column(name = "created_On")
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    private Long views;

    public Event(Long id, String annotation, Category category, int confirmedRequests, LocalDateTime createdOn, String description, LocalDateTime eventDate, User initiator, Location location, Boolean paid, int participantLimit, LocalDateTime publishedOn, Boolean requestModeration, EventState eventState, String title, Long views) {
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        if (createdOn == null) {
            this.createdOn = LocalDateTime.now();
        } else {
            this.createdOn = createdOn;
        }

        this.description = description;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        if (requestModeration == null) {
            this.requestModeration = true;
        } else {
            this.requestModeration = requestModeration;
        }
        if (eventState == null) {
            this.state = EventState.PENDING;
        } else {
            this.state = eventState;
        }
        this.title = title;
        this.views = views;
    }

    @Override
    public String toString() {
        return "Event{" +
                "datePattern='" + datePattern + '\'' +
                ", dateFormatter=" + dateFormatter +
                ", id=" + id +
                ", annotation='" + annotation + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn=" + createdOn +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", initiator=" + initiator +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", publishedOn=" + publishedOn +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}