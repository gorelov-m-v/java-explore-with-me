package ru.practicum.ewm.main_service.event.service;

import ru.practicum.ewm.main_service.event.dto.ParticipationRequestDto;

import java.util.List;

public interface UserRequestsService {
    List<ParticipationRequestDto> getUserRequests(int userId);

    ParticipationRequestDto addUserRequest(int userId, int requestId);

    ParticipationRequestDto cancelUserRequest(int userId, int requestId);

}
