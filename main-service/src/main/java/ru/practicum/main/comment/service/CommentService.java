package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createCommentByUser(NewCommentDto newCommentDto, Long userId, Long eventId);

    CommentDto updateCommentByUser(NewCommentDto newCommentDto, Long userId, Long commentId);

    void deleteCommentByUser(Long userId, Long commentId);

    List<CommentDto> getCommentsByUserIdByUser(Long userId, Integer from, Integer size);

    List<CommentDto> getCommentsByUserIdByAdmin(Long userId, Integer from, Integer size);

    CommentDto getCommentByIdByAdmin(Long commentId);

    List<CommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size);
}
