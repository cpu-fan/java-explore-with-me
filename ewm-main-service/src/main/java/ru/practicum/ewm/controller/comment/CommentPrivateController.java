package ru.practicum.ewm.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentRequestDto;
import ru.practicum.ewm.dto.comment.CommentResponseDto;
import ru.practicum.ewm.service.comment.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;

    // По моей задумке редактировать добавленный комментарий нельзя.
    // Только удалить и написать новый (например, по аналогии с Instagram).
    // И не вижу здесь смысла в получении одного комментария, типа getComment.
    // В public controller будет endpoint для получения списка комментариев для события.

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@PathVariable long userId,
                                         @RequestParam long eventId,
                                         @Valid @RequestBody CommentRequestDto comment) {
        return commentService.addComment(userId, eventId, comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId,
                              @PathVariable long commentId) {
        commentService.deleteCommentUser(userId, commentId);
    }
}
