package ru.practicum.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ViewStatsRequest {
    LocalDateTime start;
    LocalDateTime end;
    String[] uris;
    Boolean unique;
}
