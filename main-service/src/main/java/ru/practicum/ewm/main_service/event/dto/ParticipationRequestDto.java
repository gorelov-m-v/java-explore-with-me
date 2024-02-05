package ru.practicum.ewm.main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.main_service.event.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ParticipationRequestDto {
    private Integer id;
    private LocalDateTime created;
    private int event;
    private int requester;
    private RequestStatus status;
}