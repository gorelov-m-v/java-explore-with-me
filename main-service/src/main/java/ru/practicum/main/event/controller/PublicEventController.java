package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.model.enums.SortValue;
import ru.practicum.main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsWithParamsByUser(@RequestParam(name = "text", required = false) String text,
                                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                                        @RequestParam(name = "paid", required = false) Boolean paid,
                                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                        @RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable,
                                                        @RequestParam(name = "sort", required = false) SortValue sort,
                                                        @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                        HttpServletRequest request) {
        return eventService.getEventsWithParamsByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }
}