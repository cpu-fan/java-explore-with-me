package ru.practicum.ewm.service.comment;

import ru.practicum.ewm.dto.comment.CommentRequestDto;
import ru.practicum.ewm.dto.comment.CommentResponseDto;

import java.util.Collection;

public interface CommentService {

    CommentResponseDto addComment(long userId, long eventId, CommentRequestDto commentDto);

    void deleteCommentUser(long userId, long eventId);

    void deleteCommentAdmin(long eventId);

    Collection<CommentResponseDto> getEventComments(long eventId, int from, int size);
}
