package ru.practicum.ewm.main_service.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.main_service.category.dto.CategoryDto;
import ru.practicum.ewm.main_service.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventShortDto {
    private Integer id;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private long views;

    public EventShortDto(Integer id, String annotation, LocalDateTime eventDate, boolean paid, String title) {
        this.id = id;
        this.annotation = annotation;
        this.eventDate = eventDate;
        this.paid = paid;
        this.title = title;
    }
}
