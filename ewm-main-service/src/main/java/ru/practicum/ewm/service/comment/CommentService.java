package ru.practicum.ewm.service.comment;

import ru.practicum.ewm.dto.comment.CommentRequestDto;
import ru.practicum.ewm.dto.comment.CommentResponseDto;
import ru.practicum.ewm.model.comment.Comment;

import java.util.Collection;

public interface CommentService {

    CommentResponseDto addComment(long userId, long eventId, CommentRequestDto commentDto);

    void deleteCommentUser(long userId, long commentId);

    void deleteCommentAdmin(long commentId);

    Collection<CommentResponseDto> getEventComments(long eventId, int from, int size);

    Comment getCommentEntity(long commentId);

    boolean existsById(long commentId);
}
