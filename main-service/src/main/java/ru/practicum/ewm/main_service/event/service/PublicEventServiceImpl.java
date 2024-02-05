package ru.practicum.ewm.main_service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.client.ViewStatsRequest;
import ru.practicum.ewm.dto.Stat;
import ru.practicum.ewm.main_service.event.dto.EventFullDto;
import ru.practicum.ewm.main_service.event.dto.EventShortDto;
import ru.practicum.ewm.main_service.event.mapper.EventMapper;
import ru.practicum.ewm.main_service.event.model.Event;
import ru.practicum.ewm.main_service.event.model.EventState;
import ru.practicum.ewm.main_service.event.model.SortType;
import ru.practicum.ewm.main_service.event.repository.EventRepository;
import ru.practicum.ewm.main_service.event.repository.ParticipationRequestRepository;
import ru.practicum.ewm.main_service.exception.ForbiddenException;
import ru.practicum.ewm.main_service.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatsClient statsClient;

    public static int compareByViews(EventShortDto e1, EventShortDto e2) {
        return e2.getViews() > e1.getViews() ? 1 : -1;
    }

    public static int compareByEventDate(EventShortDto e1, EventShortDto e2) {
        return e2.getEventDate().compareTo(e1.getEventDate());
    }

    @Override
    public List<EventShortDto> searchEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sortBy, PageRequest page) {
        List<Event> events;
        if (rangeStart == null && rangeEnd == null) {
            if (onlyAvailable) {
                events = eventRepository.searchOnlyAvailableFutureEvents(text, categories, paid, LocalDateTime.now(), page);
            } else {
                events = eventRepository.searchFutureEvents(text, categories, paid, LocalDateTime.now(), page);
            }
        } else {
            if (onlyAvailable) {
                events = eventRepository.searchOnlyAvailableEventsWithDates(text, categories, paid, rangeStart, rangeEnd, page);
            } else {
                events = eventRepository.searchEventsWithDates(text, categories, paid, rangeStart, rangeEnd, page);
            }
        }
        List<EventShortDto> foundEvents = EventMapper.toShortDtoList(events);
        fillViews(foundEvents);
        if (SortType.EVENT_DATE.equals(sortBy)) {
            return foundEvents.stream().sorted(PublicEventServiceImpl::compareByEventDate).collect(Collectors.toList());
        } else {
            return foundEvents.stream().sorted(PublicEventServiceImpl::compareByViews).collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getEvent(int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + id));
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("Событие должно быть опубликовано.");
        }
        EventFullDto foundEvent = EventMapper.toFullDto(event);
        List<Stat> viewStats = findViews(new String[]{"/events" + id});
        foundEvent.setViews(viewStats.get(0).getHits());
        return foundEvent;
    }

    private void fillViews(List<EventShortDto> events) {
        Map<String, EventShortDto> eventsMap = events
                .stream()
                .collect(Collectors.toMap(e -> "/events/" + e.getId(), Function.identity()));
        List<Stat> stats = findViews(eventsMap.keySet().toArray(new String[eventsMap.size()]));
        for (Stat vs : stats) {
            eventsMap.get(vs.getApp()).setViews(vs.getHits());
        }
    }

    private List<Stat> findViews(String[] uris) {
        ViewStatsRequest request = new ViewStatsRequest(LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.now(), uris, false);
        return statsClient.findStatistic(request);
    }
}
