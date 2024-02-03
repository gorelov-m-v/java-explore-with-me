package ru.practicum.client;

import ru.practicum.model.dto.HitDto;
import ru.practicum.model.dto.Stat;

import java.util.List;

public interface StatsClient {
    void saveEndpoint(HitDto hitDto);

    List<Stat> findStatistic(ViewStatsRequest request);
}