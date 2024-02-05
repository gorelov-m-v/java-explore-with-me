package ru.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {
    List<EventFullDto> searchEvents(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, PageRequest page);

    EventFullDto updateEvent(int eventId, UpdateEventAdminRequest request);
}
