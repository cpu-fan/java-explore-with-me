package ru.practicum.ewm.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.comment.CommentService;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteCommentAdmin(commentId);
    }
}
