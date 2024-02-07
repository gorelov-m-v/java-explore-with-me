package ru.practicum.main.user.service;

import ru.practicum.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userModelDto);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);
}