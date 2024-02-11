package ru.practicum.main.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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
public interface EventMapper {

    @Named("setStateViaAdmin")
    static EventState setStateViaAdmin(UpdateEventAdminDto updateEventAdminDto) {
        EventState eventState = null;
        if (updateEventAdminDto.getStateAction() != null) {
            if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
                eventState = EventState.PUBLISHED;
            }
            if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.REJECT_EVENT)) {
                eventState = EventState.CANCELED;
            }
        }
        return eventState;
    }

    @Named("setPublishedOnViaAdmin")
    static LocalDateTime setPublishedOnViaAdmin(UpdateEventAdminDto updateEventAdminDto) {
        if (updateEventAdminDto.getStateAction() != null) {
            return LocalDateTime.now();
        }
        return null;
    }

    @Named("setParticipantLimit")
    static int setParticipantLimit(UpdateEventAdminDto updateEventAdminDto) {
        if (updateEventAdminDto.getParticipantLimit() != null) {
            return updateEventAdminDto.getParticipantLimit().intValue();
        }
        return 0;
    }

    @Named("setStateViaUser")
    static EventState setStateViaUser(UpdateEventUserDto updateEventUserDto) {
        EventState eventState = null;
        if (updateEventUserDto.getStateAction() != null) {
            if (updateEventUserDto.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                eventState = EventState.PENDING;
            } else {
                eventState = EventState.CANCELED;
            }
        }
        return eventState;
    }

    EventFullDto toEventFullDto(Event event);

    @Mapping(source = "category", target = "category.id")
    Event toEventModel(NewEventDto newEventDto);

    List<EventShortDto> toEventShortDtoList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);

    @Mapping(target = "annotation", expression = "java(updateEventAdminDto.getAnnotation() != null ? " +
            "updateEventAdminDto.getAnnotation() : event.getAnnotation())")
    @Mapping(target = "category", expression = "java(category != null ? " +
            "category : event.getCategory())")
    @Mapping(target = "description", expression = "java(updateEventAdminDto.getDescription() != null ? " +
            "updateEventAdminDto.getDescription() : event.getDescription())")
    @Mapping(target = "location", expression = "java(updateEventAdminDto.getLocation() != null ? " +
            "updateEventAdminDto.getLocation() : event.getLocation())")
    @Mapping(target = "paid", expression = "java(updateEventAdminDto.getPaid() != null ? " +
            "updateEventAdminDto.getPaid() : event.getPaid())")
    @Mapping(target = "participantLimit", expression = "java(updateEventAdminDto.getParticipantLimit() != null ? " +
            "updateEventAdminDto.getParticipantLimit().intValue() : event.getParticipantLimit())")
    @Mapping(target = "requestModeration", expression = "java(updateEventAdminDto.getRequestModeration() != null ? " +
            "updateEventAdminDto.getRequestModeration() : event.getRequestModeration())")
    @Mapping(target = "title", expression = "java(updateEventAdminDto.getTitle() != null ? " +
            "updateEventAdminDto.getTitle() : event.getTitle())")
    @Mapping(target = "eventDate", expression = "java(updateEventAdminDto.getEventDate() != null ? " +
            "updateEventAdminDto.getEventDate() : event.getEventDate())")
    @Mapping(target = "id", expression = "java(event.getId())")
    @Mapping(target = "state", source = "updateEventAdminDto", qualifiedByName = "setStateViaAdmin")
    @Mapping(target = "publishedOn", source = "updateEventAdminDto", qualifiedByName = "setPublishedOnViaAdmin")
    Event toEventByAdmin(UpdateEventAdminDto updateEventAdminDto, Event event, Category category);

    @Mapping(target = "annotation", expression = "java(updateEventUserDto.getAnnotation() != null ? " +
            "updateEventUserDto.getAnnotation() : event.getAnnotation())")
    @Mapping(target = "category", expression = "java(category != null ? " +
            "category : event.getCategory())")
    @Mapping(target = "description", expression = "java(updateEventUserDto.getDescription() != null ? " +
            "updateEventUserDto.getDescription() : event.getDescription())")
    @Mapping(target = "location", expression = "java(updateEventUserDto.getLocation() != null ? " +
            "updateEventUserDto.getLocation() : event.getLocation())")
    @Mapping(target = "paid", expression = "java(updateEventUserDto.getPaid() != null ? " +
            "updateEventUserDto.getPaid() : event.getPaid())")
    @Mapping(target = "participantLimit", expression = "java(updateEventUserDto.getParticipantLimit() != null ? " +
            "updateEventUserDto.getParticipantLimit().intValue() : event.getParticipantLimit())")
    @Mapping(target = "requestModeration", expression = "java(updateEventUserDto.getRequestModeration() != null ? " +
            "updateEventUserDto.getRequestModeration() : event.getRequestModeration())")
    @Mapping(target = "title", expression = "java(updateEventUserDto.getTitle() != null ? " +
            "updateEventUserDto.getTitle() : event.getTitle())")
    @Mapping(target = "eventDate", expression = "java(updateEventUserDto.getEventDate() != null ? " +
            "updateEventUserDto.getEventDate() : event.getEventDate())")
    @Mapping(target = "id", expression = "java(event.getId())")
    @Mapping(target = "state", source = "updateEventUserDto", qualifiedByName = "setStateViaUser")
    Event toEventByUser(UpdateEventUserDto updateEventUserDto, Event event, Category category);
}
