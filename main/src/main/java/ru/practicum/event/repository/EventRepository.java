package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    int countAllByCategory_Id(int catId);

    List<Event> findByIdIn(List<Integer> ids);

    List<Event> findByInitiator_Id(Integer userId, Pageable page);

    Optional<Event> findByIdAndInitiator_Id(int id, int userId);

    @Query(" SELECT e " +
            "FROM Event AS e " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> searchEventsByAdmin(@Param("users") List<Integer> users,
                                    @Param("states") List<String> states,
                                    @Param("categories") List<Integer> categories,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);
}
