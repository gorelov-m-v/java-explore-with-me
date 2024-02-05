package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.ParticipationRequestMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.ParticipationRequest;
import ru.practicum.event.model.RequestStatus;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.ParticipationRequestRepository;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersEventsServiceImpl implements UsersEventsService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public List<EventShortDto> getUserEvents(int userId, PageRequest page) {
        List<Event> events = eventRepository.findByInitiator_Id(userId, page);
        return EventMapper.toShortDtoList(events);
    }

    @Override
    public EventShortDto addUserEvent(int userId, NewEventDto eventDto) {
        checkData(eventDto.getEventDate());
        User initiator = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new ValidationException("Категория с id = " + eventDto.getCategory()
                        + " не найдена или недоступна."));
        Event event = EventMapper.toNewEntity(eventDto, initiator, category);
        return EventMapper.toShortDto(event);
    }

    @Override
    public EventFullDto getUserEvent(int userId, int eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие id = " + eventId
                        + "пользователя id = " + userId));
        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto updateUserEvent(int userId, int eventId, UpdateEventUserRequest request) {
        checkData(request.getEventDate());

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + eventId));
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации.");
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

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(int userId, int eventId) {
        List<ParticipationRequest> participationRequests = participationRequestRepository.findByEvent_Id(eventId);
        return ParticipationRequestMapper.toDtoList(participationRequests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsStatuses(int userId, int eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + eventId
                        + "пользователя id = " + userId));
        int participantLimit = event.getParticipantLimit();
        int confirmedRequestsCount = participationRequestRepository.countAllByEvent_IdAndStatus(eventId,
                RequestStatus.CONFIRMED);
        RequestStatus newStatus = request.getStatus();

        if (RequestStatus.CONFIRMED.equals(newStatus) && confirmedRequestsCount == participantLimit) {
            throw new ForbiddenException("Достигнут лимит по заявкам на данное событие.");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        List<ParticipationRequest> participationRequests = participationRequestRepository.findByIdIn(request.getRequestIds());
        boolean isLimit = false;
        for (ParticipationRequest pr : participationRequests) {
            if (!RequestStatus.PENDING.equals(pr.getStatus())) {
                throw new ForbiddenException("Статус можно изменить только у заявок, находящихся в состоянии ожидания.");
            }
            if (RequestStatus.REJECTED.equals(newStatus) || isLimit) {
                pr.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(ParticipationRequestMapper.toDto(pr));
            }
            if (RequestStatus.CONFIRMED.equals(newStatus)) {
                pr.setStatus(RequestStatus.CONFIRMED);
                confirmedRequestsCount++;
                confirmedRequests.add(ParticipationRequestMapper.toDto(pr));
                if (confirmedRequestsCount == participantLimit) {
                    isLimit = true;
                }
            }
        }
        participationRequestRepository.saveAll(participationRequests);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

    private void checkData(LocalDateTime dateTime) {
        if (dateTime == null) return;
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: "
                    + dateTime);
        }
    }
}
