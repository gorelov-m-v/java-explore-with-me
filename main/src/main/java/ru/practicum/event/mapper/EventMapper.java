package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventShortDto toShortDto(Event event) {
        EventShortDto shortDto = new EventShortDto(event.getId(), event.getAnnotation(),
                event.getEventDate(), event.isPaid(), event.getTitle());
        shortDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        shortDto.setInitiator(UserMapper.toShortDto(event.getInitiator()));
        return shortDto;
    }

    public static List<EventShortDto> toShortDtoList(Collection<Event> events) {
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
