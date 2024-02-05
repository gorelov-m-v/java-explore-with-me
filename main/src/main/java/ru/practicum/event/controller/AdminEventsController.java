package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.AdminEventsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
@RequiredArgsConstructor
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @GetMapping
    public List<EventFullDto> searchEvents(@RequestParam List<Integer> users,
                                           @RequestParam List<String> states,
                                           @RequestParam List<Integer> categories,
                                           @RequestParam LocalDateTime rangeStart,
                                           @RequestParam LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return adminEventsService.searchEvents(users, states, categories, rangeStart,
                rangeEnd, page);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable int eventId,
                                    @RequestBody UpdateEventAdminRequest request) {
        return adminEventsService.updateEvent(eventId, request);
    }
}
