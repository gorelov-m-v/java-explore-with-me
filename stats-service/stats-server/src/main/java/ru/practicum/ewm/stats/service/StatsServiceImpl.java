package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.Stat;
import ru.practicum.ewm.stats.exception.InvalidPeriodException;
import ru.practicum.ewm.stats.mapper.StatsMapper;
import ru.practicum.ewm.stats.model.Endpoint;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void save(HitDto hitDto) {
        Endpoint endpoint = StatsMapper.toEndpoint(hitDto);
        statsRepository.save(endpoint);
    }

    @Override
    public List<Stat> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new InvalidPeriodException("end не должно быть раньше чем before");
        }

        if (uris.isEmpty()) {
            if (unique) {
                return statsRepository.findUniqueStat(start, end);
            } else {
                return statsRepository.findNotUniqueStat(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.findUniqueStatWithGivenUris(start, end, uris);
            } else {
                return statsRepository.findNotUniqueStatWithGivenUris(start, end, uris);
            }
        }
    }
}