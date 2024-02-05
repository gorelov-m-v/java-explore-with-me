package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    int countAllByCategory_Id(int catId);

    List<Event> findByIdIn(List<Integer> ids);
}
