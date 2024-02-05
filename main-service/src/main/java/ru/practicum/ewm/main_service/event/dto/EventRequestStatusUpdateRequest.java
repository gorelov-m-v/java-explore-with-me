package ru.practicum.ewm.main_service.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.main_service.event.model.RequestStatus;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}
