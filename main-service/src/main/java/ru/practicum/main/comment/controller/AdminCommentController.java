package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.service.CommentService;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/user/{userId}")
    public List<CommentDto> getCommentsByUserIdByUser(@PathVariable(value = "userId") Long userId,
                                                      @PositiveOrZero
                                                      @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                      @Positive
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return commentService.getCommentsByUserIdByAdmin(userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentByIdByAdmin(@PathVariable(value = "commentId") Long commentId) {

        return commentService.getCommentByIdByAdmin(commentId);
    }
}
