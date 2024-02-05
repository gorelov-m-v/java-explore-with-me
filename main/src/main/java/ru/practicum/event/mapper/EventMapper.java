package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
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

    public static EventFullDto toFullDto(Event event) {
        EventFullDto fullDto = new EventFullDto();
        fullDto.setId(event.getId());
        fullDto.setAnnotation(event.getAnnotation());
        fullDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        fullDto.setEventDate(event.getEventDate());
        fullDto.setCreatedOn(event.getCreatedOn());
        fullDto.setPublishedOn(event.getPublishedOn());
        fullDto.setDescription(event.getDescription());
        fullDto.setLocation(new Location(event.getLat(), event.getLon()));
        fullDto.setInitiator(UserMapper.toShortDto(event.getInitiator()));
        fullDto.setPaid(event.isPaid());
        fullDto.setParticipantLimit(event.getParticipantLimit());
        fullDto.setRequestModeration(event.isRequestModeration());
        fullDto.setState(event.getState());
        fullDto.setTitle(event.getTitle());
        return fullDto;
    }

    public static Event toNewEntity(NewEventDto dto, User initiator, Category category) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLon(dto.getLocation().getLon());
        event.setLat(dto.getLocation().getLat());
        event.setCreatedOn(LocalDateTime.now());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.isRequestModeration());
        event.setPaid(dto.isPaid());
        event.setTitle(dto.getTitle());
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        return event;
    }
}
