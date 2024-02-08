package ru.practicum.service;


import ru.practicum.dto.HitDto;
import ru.practicum.dto.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void save(HitDto hitDto);

    List<Stat> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
