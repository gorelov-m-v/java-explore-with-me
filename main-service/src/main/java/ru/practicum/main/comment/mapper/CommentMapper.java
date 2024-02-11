package ru.practicum.main.comment.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toListCommentDto(List<Comment> comment);

}