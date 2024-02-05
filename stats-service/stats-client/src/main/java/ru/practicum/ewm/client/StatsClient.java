package ru.practicum.ewm.client;

import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.Stat;

import java.util.List;

public interface StatsClient {
    void saveEndpoint(HitDto hitDto);

    List<Stat> findStatistic(ViewStatsRequest request);
}