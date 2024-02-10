package ru.practicum.server.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.exception.ValidationRequestException;
import ru.practicum.server.mappers.EndpointHitMapper;
import ru.practicum.server.models.ViewStats;
import ru.practicum.server.repositories.StatServerRepository;
import ru.practicum.server.services.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatServerRepository statServerRepository;
    private final EndpointHitMapper endpointHitMapper;

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        statServerRepository.save(endpointHitMapper.toEntity(endpointHitDto));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new ValidationRequestException("end не должно быть раньше чем before");
        }
        if (uris == null) {
            if (unique) {
                return statServerRepository.findUniqueStat(start, end);

            } else {
                return statServerRepository.findNotUniqueStat(start, end);
            }
        } else {
            if (unique) {
                return statServerRepository.findUniqueStatWithGivenUris(start, end, uris);
            } else {
                return statServerRepository.findNotUniqueStatWithGivenUris(start, end, uris);
            }
        }
    }
}