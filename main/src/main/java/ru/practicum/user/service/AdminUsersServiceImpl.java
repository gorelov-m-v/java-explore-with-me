package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUsersServiceImpl implements AdminUsersService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, PageRequest page) {
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(page).getContent();
        } else {
            users = userRepository.findByIdIn(ids, page);
        }
        return UserMapper.toDtoList(users);
    }

    @Override
    public UserDto saveUser(NewUserRequest request) {
        User user = userRepository.save(UserMapper.toEntity(request));
        return UserMapper.toDto(user);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        userRepository.deleteById(userId);
    }
}
