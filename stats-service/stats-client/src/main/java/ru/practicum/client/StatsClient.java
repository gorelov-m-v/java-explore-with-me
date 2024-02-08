package ru.practicum.client;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.Stat;

import java.util.List;

public interface StatsClient {
    void saveEndpoint(HitDto hitDto);

    List<Stat> findStatistic(ViewStatsRequest request);
}