package ru.practicum.ewm.main_service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main_service.category.model.Category;
import ru.practicum.ewm.main_service.category.repository.CategoryRepository;
import ru.practicum.ewm.main_service.event.dto.EventFullDto;
import ru.practicum.ewm.main_service.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.main_service.event.mapper.EventMapper;
import ru.practicum.ewm.main_service.event.model.Event;
import ru.practicum.ewm.main_service.event.model.EventState;
import ru.practicum.ewm.main_service.event.model.StateAction;
import ru.practicum.ewm.main_service.event.repository.EventRepository;
import ru.practicum.ewm.main_service.exception.ForbiddenException;
import ru.practicum.ewm.main_service.exception.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventsServiceImpl implements AdminEventsService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventFullDto> searchEvents(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, PageRequest page) {
        List<Event> events = eventRepository.searchEventsByAdmin(users, states, categories, rangeStart, rangeEnd, page);
        return EventMapper.toFullDtoList(events);
    }

    @Override
    public EventFullDto updateEvent(int eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId));
        if (StateAction.valueOf(request.getStateAction().toUpperCase()).equals(StateAction.PUBLISH_EVENT)) {
            if (request.getEventDate() != null && request.getEventDate().isBefore(LocalDateTime.now().plusHours(1))
                    || request.getEventDate() == null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ForbiddenException("Дата начала изменяемого события должна быть не ранее чем за час" +
                        " от даты публикации.");
            } else {
                if (!EventState.PENDING.equals(event.getState())) {
                    throw new ForbiddenException("Cобытие можно публиковать, только если оно в состоянии ожидания публикации.");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        } else {
            if (EventState.PUBLISHED.equals(event.getState())) {
                throw new ForbiddenException("Cобытие можно отклонить, только если оно еще не опубликовано.");
            }
            event.setState(EventState.CANCELED);
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getCategory() != null) {
            Category newCat = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new ValidationException("Категория с id = " + request.getCategory()
                            + " не найдена или недоступна."));
            event.setCategory(newCat);
        }
        eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }
}
