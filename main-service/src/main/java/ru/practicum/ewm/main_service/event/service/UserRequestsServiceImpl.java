package ru.practicum.ewm.main_service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main_service.event.dto.ParticipationRequestDto;
import ru.practicum.ewm.main_service.event.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.main_service.event.model.Event;
import ru.practicum.ewm.main_service.event.model.EventState;
import ru.practicum.ewm.main_service.event.model.ParticipationRequest;
import ru.practicum.ewm.main_service.event.model.RequestStatus;
import ru.practicum.ewm.main_service.event.repository.EventRepository;
import ru.practicum.ewm.main_service.event.repository.ParticipationRequestRepository;
import ru.practicum.ewm.main_service.exception.ForbiddenException;
import ru.practicum.ewm.main_service.exception.NotFoundException;
import ru.practicum.ewm.main_service.user.model.User;
import ru.practicum.ewm.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRequestsServiceImpl implements UserRequestsService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id = " + userId));
        List<ParticipationRequest> requests = participationRequestRepository.findByRequester_Id(userId);
        return ParticipationRequestMapper.toDtoList(requests);
    }

    @Override
    public ParticipationRequestDto addUserRequest(int userId, int eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id = " + userId));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Инициатор события не может добавить запрос на участие в своём событии.");
        }
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии.");
        }
        int userRequestsCount = participationRequestRepository.countAllByRequester_IdAndEventId(userId, eventId);
        if (userRequestsCount > 0) {
            throw new ForbiddenException("Нельзя добавить повторный запрос.");
        }

        int participationRequestCount = participationRequestRepository.countAllByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (participationRequestCount == event.getParticipantLimit()) {
            throw new ForbiddenException("У события достигнут лимит запросов на участие.");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        if (event.isRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        request = participationRequestRepository.save(request);
        return ParticipationRequestMapper.toDto(request);
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(int userId, int requestId) {
        ParticipationRequest request = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос с id = " + requestId));
        request.setStatus(RequestStatus.REJECTED);
        participationRequestRepository.save(request);
        return ParticipationRequestMapper.toDto(request);
    }
}

