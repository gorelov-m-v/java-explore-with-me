package ru.practicum.ewm.stats.mapper;

import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.stats.model.Endpoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {

    public static Endpoint toEndpoint(HitDto hitDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Endpoint endpoint = new Endpoint();
        endpoint.setIp(hitDto.getIp());
        endpoint.setUri(hitDto.getUri());
        endpoint.setApp(hitDto.getApp());
        endpoint.setDateTime(LocalDateTime.parse(hitDto.getTimestamp(), formatter));

        return endpoint;
    }
}
