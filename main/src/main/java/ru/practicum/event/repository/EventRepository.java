package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    int countAllByCategory_Id(int catId);

    List<Event> findByIdIn(List<Integer> ids);

    List<Event> findByInitiator_Id(Integer userId, Pageable page);

    Optional<Event> findByIdAndInitiator_Id(int id, int userId);
}
