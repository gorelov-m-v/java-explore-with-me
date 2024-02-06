package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.dto.Stat;
import ru.practicum.ewm.stats.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Endpoint, Integer> {
    @Query("SELECT new ru.practicum.ewm.dto.Stat(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM Endpoint AS e " +
            "WHERE e.dateTime BETWEEN :startDate AND :endDate " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<Stat> findUniqueStatWithGivenUris(@Param("startDate") LocalDateTime start,
                                           @Param("endDate") LocalDateTime end,
                                           @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.Stat(e.app, e.uri, COUNT(e)) " +
            "FROM Endpoint AS e " +
            "WHERE e.dateTime BETWEEN :startDate AND :endDate " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<Stat> findNotUniqueStatWithGivenUris(@Param("startDate") LocalDateTime start,
                                              @Param("endDate") LocalDateTime end,
                                              @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.Stat(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM Endpoint AS e " +
            "WHERE e.dateTime BETWEEN :startDate AND :endDate " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<Stat> findUniqueStat(@Param("startDate") LocalDateTime start,
                              @Param("endDate") LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.Stat(e.app, e.uri, COUNT(e)) " +
            "FROM Endpoint AS e " +
            "WHERE e.dateTime BETWEEN :startDate AND :endDate " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<Stat> findNotUniqueStat(@Param("startDate") LocalDateTime start,
                                 @Param("endDate") LocalDateTime end);
}