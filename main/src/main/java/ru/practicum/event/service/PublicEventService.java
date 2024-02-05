package ru.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.SortType;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> searchEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sortBy, PageRequest page);

    EventFullDto getEvent(int id);
}
