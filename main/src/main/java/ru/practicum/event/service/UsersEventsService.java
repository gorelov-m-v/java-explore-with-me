package ru.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface UsersEventsService {
    List<EventShortDto> getUserEvents(int userId, PageRequest page);

    EventShortDto addUserEvent(int userId, NewEventDto eventDto);

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto updateUserEvent(int userId, int eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getUserEventRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequestsStatuses(int userId, int eventId, EventRequestStatusUpdateRequest request);
}
