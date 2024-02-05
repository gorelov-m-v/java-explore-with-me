package ru.practicum.ewm.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main_service.event.dto.EventFullDto;
import ru.practicum.ewm.main_service.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main_service.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main_service.event.dto.EventShortDto;
import ru.practicum.ewm.main_service.event.dto.NewEventDto;
import ru.practicum.ewm.main_service.event.dto.ParticipationRequestDto;
import ru.practicum.ewm.main_service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main_service.event.service.UsersEventsService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UsersEventsController {
    private final UsersEventsService usersEventsService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable int userId, @RequestParam int from, @RequestParam int size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<EventShortDto> foundEvents = usersEventsService.getUserEvents(userId, page);
        return foundEvents;
    }

    @PostMapping("/{userId}/events")
    public EventShortDto addUserEvent(@PathVariable int userId, @RequestBody NewEventDto eventDto) {
        EventShortDto addedEvent = usersEventsService.addUserEvent(userId, eventDto);
        return addedEvent;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable int userId, @PathVariable int eventId) {
        EventFullDto foundEvent = usersEventsService.getUserEvent(userId, eventId);
        return foundEvent;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable int userId, @PathVariable int eventId,
                                        @RequestBody UpdateEventUserRequest request) {
        return usersEventsService.updateUserEvent(userId, eventId, request);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable int userId, @PathVariable int eventId) {
        return usersEventsService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatuses(@PathVariable int userId, @PathVariable int eventId,
                                                                 @RequestBody EventRequestStatusUpdateRequest request) {
        return usersEventsService.updateRequestsStatuses(userId, eventId, request);
    }
}