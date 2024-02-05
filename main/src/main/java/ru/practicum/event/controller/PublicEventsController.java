package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.SortType;
import ru.practicum.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class PublicEventsController {
    private final StatsClient statsClient;
    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventShortDto> searchEvents(@RequestParam String text, @RequestParam List<Integer> categories,
                                            @RequestParam Boolean paid, @RequestParam LocalDateTime rangeStart,
                                            @RequestParam LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam String sortBy,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        List<EventShortDto> foundEvents;
        try {
            PageRequest page = PageRequest.of(from / size, size);
            foundEvents = publicEventService.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                    SortType.valueOf(sortBy.toUpperCase()), page);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Неверно указан параметр сортировки.");
        }
        addToStatistic("/events", request.getRemoteAddr(), request.getRequestURI());
        return foundEvents;
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable int id, HttpServletRequest request) {
        EventFullDto foundEvent = publicEventService.getEvent(id);
        addToStatistic("/event/" + id, request.getRemoteAddr(), request.getRequestURI());
        return foundEvent;
    }

    private void addToStatistic(String app, String ip, String uri) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(app);
        hitDto.setIp(ip);
        hitDto.setUri(uri);
        hitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsClient.saveEndpoint(hitDto);
    }
}
