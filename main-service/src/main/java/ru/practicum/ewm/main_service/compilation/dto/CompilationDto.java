package ru.practicum.ewm.main_service.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.main_service.event.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
public class CompilationDto {
    private Integer id;
    private boolean pinned;
    private String title;
    private Set<EventShortDto> events;

    public CompilationDto(Integer id, boolean pinned, String title) {
        this.id = id;
        this.pinned = pinned;
        this.title = title;
    }
}
