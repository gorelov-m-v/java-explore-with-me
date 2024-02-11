package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.NewCommentDto;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.CommentsRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService  {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;
    private final CommentsRepository commentsRepository;

    @Override
    public CommentDto createCommentByUser(NewCommentDto newCommentDto, Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Ивент не найден."));

        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setEventId(event.getId());
        comment.setText(newCommentDto.getText());
        comment.setCreated(Instant.now().getEpochSecond());
        comment.setUpdated(null);
        comment.setIsDeleted(false);

        return commentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Override
    public CommentDto updateCommentByUser(NewCommentDto newCommentDto, Long userId, Long commentId) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                "Комментарий не найден."));
        if (comment.getIsDeleted()) {
            throw new ConflictException("Комментарий удален.");
        }
        checkCommentByUser(comment, userId);

        comment.setText(newCommentDto.getText());
        comment.setUpdated(Instant.now().getEpochSecond());

        return commentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                "Комментарий не найден."));
        if (comment.getIsDeleted()) {
            throw new ConflictException("Комментарий уже удален.");
        }
        checkCommentByUser(comment, userId);

        comment.setUpdated(Instant.now().getEpochSecond());
        comment.setIsDeleted(true);

        commentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByUserIdByUser(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        List<Comment> eventComments = commentsRepository.findAllByUserId(userId, page);

        return commentMapper.toListCommentDto(eventComments.stream()
                .filter(ec -> !ec.getIsDeleted()).collect(Collectors.toList()));
    }

    @Override
    public List<CommentDto> getCommentsByUserIdByAdmin(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        List<Comment> eventComments = commentsRepository.findAllByUserId(userId, page);

        return commentMapper.toListCommentDto(eventComments);
    }

    @Override
    public CommentDto getCommentByIdByAdmin(Long commentId) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                "Комментарий не найден."));

        return commentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Ивент не найден."));
        List<Comment> eventComments = commentsRepository.findAllByEventId(eventId, page);

        return commentMapper.toListCommentDto(eventComments.stream()
                .filter(ec -> !ec.getIsDeleted()).collect(Collectors.toList()));
    }

    private void  checkCommentByUser(Comment comment, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new NotFoundException("Только хозяин может изменить/удалить комментарий.");
        }
    }
}
