package ru.practicum.main.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventAdminDto;
import ru.practicum.main.event.dto.UpdateEventUserDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.enums.EventState;
import ru.practicum.main.event.model.enums.StateActionForAdmin;
import ru.practicum.main.event.model.enums.StateActionForUser;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class EventMapper {

    public abstract EventFullDto toEventFullDto(Event event);

    @Mapping(source = "category", target = "category.id")
    public abstract Event toEventModel(NewEventDto newEventDto);

    public abstract List<EventShortDto> toEventShortDtoList(List<Event> events);

    public abstract List<EventFullDto> toEventFullDtoList(List<Event> events);

    public Event toEventByAdmin(UpdateEventAdminDto updateEventAdminDto, Event event, Category category) {
        if (updateEventAdminDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminDto.getAnnotation());
        }
        if (updateEventAdminDto.getCategory() != null) {
            event.setCategory(category);
        }
        if (updateEventAdminDto.getDescription() != null) {
            event.setDescription(updateEventAdminDto.getDescription());
        }
        if (updateEventAdminDto.getLocation() != null) {
            event.setLocation(updateEventAdminDto.getLocation());
        }
        if (updateEventAdminDto.getPaid() != null) {
            event.setPaid(updateEventAdminDto.getPaid());
        }
        if (updateEventAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminDto.getParticipantLimit().intValue());
        }
        if (updateEventAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminDto.getRequestModeration());
        }
        if (updateEventAdminDto.getTitle() != null) {
            event.setTitle(updateEventAdminDto.getTitle());
        }
        if (updateEventAdminDto.getStateAction() != null) {
            if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventAdminDto.getEventDate() != null) {
            event.setEventDate(updateEventAdminDto.getEventDate());
        }
        return event;
    }

    public Event toEventByUser(UpdateEventUserDto updateEventUserDto, Event event, Category category) {
        if (updateEventUserDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserDto.getAnnotation());
        }
        if (updateEventUserDto.getCategory() != null) {
            event.setCategory(category);
        }
        if (updateEventUserDto.getDescription() != null) {
            event.setDescription(updateEventUserDto.getDescription());
        }
        if (updateEventUserDto.getEventDate() != null) {
            event.setEventDate(updateEventUserDto.getEventDate());
        }
        if (updateEventUserDto.getLocation() != null) {
            event.setLocation(updateEventUserDto.getLocation());
        }
        if (updateEventUserDto.getPaid() != null) {
            event.setPaid(updateEventUserDto.getPaid());
        }
        if (updateEventUserDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserDto.getParticipantLimit().intValue());
        }
        if (updateEventUserDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserDto.getRequestModeration());
        }
        if (updateEventUserDto.getTitle() != null) {
            event.setTitle(updateEventUserDto.getTitle());
        }
        if (updateEventUserDto.getStateAction() != null) {
            if (updateEventUserDto.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }
        return event;
    }
}