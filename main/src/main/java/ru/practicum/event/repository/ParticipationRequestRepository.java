package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.ParticipationRequest;
import ru.practicum.event.model.RequestStatus;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findByEvent_Id(int eventId);

    List<ParticipationRequest> findByRequester_Id(int eventId);

    int countAllByRequester_IdAndEventId(int userId, int eventId);

    int countAllByEvent_IdAndStatus(int eventId, RequestStatus status);

    List<ParticipationRequest> findByIdIn(List<Integer> ids);
}
