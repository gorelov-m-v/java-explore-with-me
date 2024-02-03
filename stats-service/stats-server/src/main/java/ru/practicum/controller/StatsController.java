package ru.practicum.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.Stat;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(@RequestBody @Valid HitDto hitDto) {
        statsService.save(hitDto);
    }

    @GetMapping("/stats")
    public List<Stat> get(@RequestParam(name = "start") @NonNull String start,
                          @RequestParam(name = "end") @NonNull String end,
                          @RequestParam(name = "uris", defaultValue = "", required = false) List<String> uris,
                          @RequestParam(name = "unique", defaultValue = "false") boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Stat> stats = statsService.get(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter),
                uris,
                unique);

        return stats;
    }
}