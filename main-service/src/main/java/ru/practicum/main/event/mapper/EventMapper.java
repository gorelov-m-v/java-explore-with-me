package ru.practicum.main.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.model.Event;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface EventMapper {
    EventFullDto toEventFullDto(Event event);

    @Mapping(source = "category", target = "category.id")
    Event toEventModel(NewEventDto newEventDto);

    List<EventShortDto> toEventShortDtoList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);
}