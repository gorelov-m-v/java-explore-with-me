package ru.practicum.ewm.main_service.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.main_service.user.dto.NewUserRequest;
import ru.practicum.ewm.main_service.user.dto.UserDto;

import java.util.List;

public interface AdminUsersService {
    List<UserDto> getAllUsers(List<Integer> ids, PageRequest page);

    UserDto saveUser(NewUserRequest request);

    void deleteUser(int userId);
}
