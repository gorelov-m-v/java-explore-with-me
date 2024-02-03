package ru.practicum.mapper;

import ru.practicum.model.Endpoint;
import ru.practicum.model.dto.HitDto;

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
