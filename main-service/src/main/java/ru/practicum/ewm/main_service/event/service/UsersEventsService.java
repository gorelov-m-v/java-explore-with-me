package ru.practicum.ewm.main_service.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.main_service.event.dto.EventFullDto;
import ru.practicum.ewm.main_service.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main_service.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main_service.event.dto.EventShortDto;
import ru.practicum.ewm.main_service.event.dto.NewEventDto;
import ru.practicum.ewm.main_service.event.dto.ParticipationRequestDto;
import ru.practicum.ewm.main_service.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface UsersEventsService {
    List<EventShortDto> getUserEvents(int userId, PageRequest page);

    EventShortDto addUserEvent(int userId, NewEventDto eventDto);

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto updateUserEvent(int userId, int eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getUserEventRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequestsStatuses(int userId, int eventId, EventRequestStatusUpdateRequest request);
}
