package ru.practicum.server.services;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}