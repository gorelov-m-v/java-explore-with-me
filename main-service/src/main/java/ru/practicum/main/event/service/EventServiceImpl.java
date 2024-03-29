package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.constants.Pattern;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventAdminDto;
import ru.practicum.main.event.dto.UpdateEventUserDto;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.mapper.TestMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.enums.EventState;
import ru.practicum.main.event.model.enums.SortValue;
import ru.practicum.main.event.model.enums.StateActionForAdmin;
import ru.practicum.main.event.model.enums.StateActionForUser;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.exceptions.WrongTimeException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final TestMapper testMapper;
    private final UserRepository userRepository;
    private final StatClient statClient;
    private final EntityManager entityManager;
    private final String datePattern = Pattern.DATE;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена."));
        LocalDateTime eventDate = newEventDto.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongTimeException("eventDate: Должно содержать еще не наступившую дату." + eventDate);
        }
        Event event = eventMapper.toEventModel(newEventDto);
        event.setCategory(category);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя с id = %s не существует.", userId)));
        event.setInitiator(user);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventMapper.toEventShortDtoList(eventRepository.findAllByInitiatorId(userId, page).toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminDto updateEventAdminDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format(
                "Ивента с id = %s не существует.", eventId)));
        Category category = null;

        if (updateEventAdminDto == null) {
            return eventMapper.toEventFullDto(event);
        }

        if (updateEventAdminDto.getCategory() != null) {
            category = categoryRepository.findById(updateEventAdminDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена."));
        }

        if (updateEventAdminDto.getStateAction() != null) {
            if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new ConflictException("Ивент уже опубликован.");
                }
                if (event.getState().equals(EventState.CANCELED)) {
                    throw new ConflictException("Ивент уже отменен.");
                }
            } else if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.REJECT_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new ConflictException("Нельзя отменить опубликованный ивент.");
                }
            }
        }

        if (updateEventAdminDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventAdminDto.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new WrongTimeException("Нельзя изменить дату ивента за час до начала.");
            }
        }

        return eventMapper.toEventFullDto(eventRepository.save(
                testMapper.toEventByAdmin(updateEventAdminDto, event, category)));
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserDto updateEventUserDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Ивент не найден."));
        Category category = null;

        if (updateEventUserDto == null) {
            return eventMapper.toEventFullDto(event);
        }

        if (updateEventUserDto.getStateAction() != StateActionForUser.SEND_TO_REVIEW && event.getPublishedOn() != null) {
            throw new ConflictException("Ивент уже опубликован.");
        }

        if (updateEventUserDto.getCategory() != null) {
            category = categoryRepository.findById(updateEventUserDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена."));
        }

        if (updateEventUserDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventUserDto.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new WrongTimeException("Нельзя изменить дату ивента за час до начала.");
            }
        }

        return eventMapper.toEventFullDto(eventRepository.save(
                testMapper.toEventByUser(updateEventUserDto, event, category)));
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        return eventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(
                eventId, userId).orElseThrow(() -> new NotFoundException("Ивента не найдено.")));
    }

    @Override
    public List<EventFullDto> getEventsWithParamsByAdmin(List<Long> users, EventState states, List<Long> categoriesId, String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, dateFormatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, dateFormatter) : null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        if (categoriesId != null && categoriesId.size() > 0) {
            Predicate containCategories = root.get("category").in(categoriesId);
            criteria = builder.and(criteria, containCategories);
        }

        if (users != null && users.size() > 0) {
            Predicate containUsers = root.get("initiator").in(users);
            criteria = builder.and(criteria, containUsers);
        }

        if (states != null) {
            Predicate containStates = root.get("state").in(states);
            criteria = builder.and(criteria, containStates);
        }

        if (rangeStart != null) {
            Predicate greaterTime = builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start);
            criteria = builder.and(criteria, greaterTime);
        }
        if (rangeEnd != null) {
            Predicate lessTime = builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end);
            criteria = builder.and(criteria, lessTime);
        }

        query.select(root).where(criteria);
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (events.size() == 0) {
            return new ArrayList<>();
        }

        setView(events);
        return eventMapper.toEventFullDtoList(events);
    }

    @Override
    public List<EventFullDto> getEventsWithParamsByUser(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                        String rangeEnd, Boolean onlyAvailable, SortValue sort, Integer from, Integer size, HttpServletRequest request) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, dateFormatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, dateFormatter);
        }
        checkDateTime(start, end);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        if (text != null) {
            Predicate annotationContain = builder.like(builder.lower(root.get("annotation")),
                    "%" + text.toLowerCase() + "%");
            Predicate descriptionContain = builder.like(builder.lower(root.get("description")),
                    "%" + text.toLowerCase() + "%");
            Predicate containText = builder.or(annotationContain, descriptionContain);

            criteria = builder.and(criteria, containText);
        }

        if (categories != null && categories.size() > 0) {
            Predicate containStates = root.get("category").in(categories);
            criteria = builder.and(criteria, containStates);
        }

        if (paid != null) {
            Predicate isPaid;
            if (paid) {
                isPaid = builder.isTrue(root.get("paid"));
            } else {
                isPaid = builder.isFalse(root.get("paid"));
            }
            criteria = builder.and(criteria, isPaid);
        }

        if (rangeStart != null) {
            Predicate greaterTime = builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start);
            criteria = builder.and(criteria, greaterTime);
        }
        if (rangeEnd != null) {
            Predicate lessTime = builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end);
            criteria = builder.and(criteria, lessTime);
        }

        query.select(root).where(criteria).orderBy(builder.asc(root.get("eventDate")));
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (onlyAvailable) {
            events = events.stream()
                    .filter((event -> event.getConfirmedRequests() < (long) event.getParticipantLimit()))
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            if (sort.equals(SortValue.EVENT_DATE)) {
                events = events.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
            } else {
                events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
            }
        }

        if (events.size() == 0) {
            return new ArrayList<>();
        }

        setView(events);
        sendStat(events, request);
        return eventMapper.toEventFullDtoList(events);
    }

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublishedOnIsNotNull(id).orElseThrow(() -> new NotFoundException(String.format("Can't find event with id = %s event doesn't exist", id)));
        setView(event);
        sendStat(event, request);
        return eventMapper.toEventFullDto(event);
    }

    public void sendStat(Event event, HttpServletRequest request) {
        statClient.addStats(createEndpoint(request));
        sendStatForTheEvent(event, request.getRemoteAddr(), LocalDateTime.now());
    }

    public void sendStat(List<Event> events, HttpServletRequest request) {
        statClient.addStats(createEndpoint(request));
        sendStatForEveryEvent(events, request.getRemoteAddr(), LocalDateTime.now());
    }

    private void sendStatForTheEvent(Event event, String remoteAddr, LocalDateTime now) {
        EndpointHitDto requestDto = createEndpoint(event, remoteAddr, now);
        statClient.addStats(requestDto);
    }

    private void sendStatForEveryEvent(List<Event> events, String remoteAddr, LocalDateTime now) {
        events.forEach(e -> statClient.addStats(createEndpoint(e, remoteAddr, now)));
    }

    public void setView(List<Event> events) {
        LocalDateTime start = events.get(0).getCreatedOn();
        List<String> uris = new ArrayList<>();
        Map<String, Event> eventsUri = new HashMap<>();
        String uri = "";
        for (Event event : events) {
            if (start.isBefore(event.getCreatedOn())) {
                start = event.getCreatedOn();
            }
            uri = "/events/" + event.getId();
            uris.add(uri);
            eventsUri.put(uri, event);
            event.setViews(0L);
        }

        String startTime = start.format(DateTimeFormatter.ofPattern(Pattern.DATE));
        String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Pattern.DATE));

        List<ViewStatsDto> stats = getStats(startTime, endTime, uris);
        stats.forEach((stat) ->
                eventsUri.get(stat.getUri()).setViews(stat.getHits()));
    }

    public void setView(Event event) {
        String startTime = event.getCreatedOn().format(dateFormatter);
        String endTime = LocalDateTime.now().format(dateFormatter);
        List<String> uris = List.of("/events/" + event.getId());

        List<ViewStatsDto> stats = getStats(startTime, endTime, uris);
        if (stats.size() == 1) {
            event.setViews(stats.get(0).getHits() + 1);
        } else {
            event.setViews(1L);
        }
    }

    private EndpointHitDto createEndpoint(HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";

        EndpointHitDto requestDto = new EndpointHitDto();
        requestDto.setTimestamp(now.format(dateFormatter));
        requestDto.setUri("/events");
        requestDto.setApp(nameService);
        requestDto.setIp(remoteAddr);

        return requestDto;
    }

    private EndpointHitDto createEndpoint(Event event, String remoteAddr, LocalDateTime now) {
        EndpointHitDto requestDto = new EndpointHitDto();
        requestDto.setTimestamp(now.format(dateFormatter));
        requestDto.setUri("/events/" + event.getId());
        requestDto.setApp("main-service");
        requestDto.setIp(remoteAddr);
        return requestDto;
    }

    private EndpointHitDto createEndpoint(HttpServletRequest request, Event event) {
        LocalDateTime now = LocalDateTime.now();
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";

        EndpointHitDto requestDto = new EndpointHitDto();
        requestDto.setTimestamp(now.format(dateFormatter));
        requestDto.setUri("/events" + event.getId());
        requestDto.setApp(nameService);
        requestDto.setIp(remoteAddr);

        return requestDto;
    }

    private List<ViewStatsDto> getStats(String startTime, String endTime, List<String> uris) {
        return statClient.getStats(startTime, endTime, uris, false);
    }

    private void checkDateTime(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            start = LocalDateTime.now().minusYears(100);
        }
        if (end == null) {
            end = LocalDateTime.now();
        }
        if (start.isAfter(end)) {
            throw new WrongTimeException("start не может быть позже end.");
        }
    }
}