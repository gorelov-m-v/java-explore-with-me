package ru.practicum.ewm.main_service.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.main_service.event.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventUserRequest {
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
