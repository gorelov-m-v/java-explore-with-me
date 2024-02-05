package ru.practicum.ewm.stats.service;


import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void save(HitDto hitDto);

    List<Stat> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
