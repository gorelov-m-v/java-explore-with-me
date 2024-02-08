package ru.practicum.main.event.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.event.dto.RequestDto;
import ru.practicum.main.event.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDtoList(List<Request> requests);
}