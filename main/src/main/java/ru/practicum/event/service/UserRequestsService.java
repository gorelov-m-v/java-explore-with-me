package ru.practicum.event.service;

import ru.practicum.event.dto.ParticipationRequestDto;

import java.util.List;

public interface UserRequestsService {
    List<ParticipationRequestDto> getUserRequests(int userId);

    ParticipationRequestDto addUserRequest(int userId, int requestId);

    ParticipationRequestDto cancelUserRequest(int userId, int requestId);

}
