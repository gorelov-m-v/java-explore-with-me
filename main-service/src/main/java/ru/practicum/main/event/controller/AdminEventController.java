package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.UpdateEventAdminDto;
import ru.practicum.main.event.model.enums.EventState;
import ru.practicum.main.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "eventId") Long eventId, @Valid @RequestBody UpdateEventAdminDto updateEventAdminDto) {
        return eventService.updateEvent(eventId, updateEventAdminDto);

    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                        @RequestParam(name = "states", required = false) EventState states,
                                        @RequestParam(name = "categories", required = false) List<Long> categoriesId,
                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                        @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsWithParamsByAdmin(users, states, categoriesId, rangeStart, rangeEnd, from, size);
    }
}