package ru.practicum.ewm.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.comment.CommentRequestDto;
import ru.practicum.ewm.dto.comment.CommentResponseDto;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.errorhandler.exceptions.ValidationException;
import ru.practicum.ewm.mapper.comment.CommentMapper;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.comment.CommentRepository;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.user.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final EventService eventService;

    private final CommentMapper mapper;

    // Комментарий может добавлять любой авторизованный пользователь любому событию,
    // необходимое условие - событие должно быть опубликовано.
    @Override
    public CommentResponseDto addComment(long userId, long eventId, CommentRequestDto commentDto) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventEntity(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            String message = "Событие eventId = " + eventId + " не опубликовано. Комментарий не возможен.";
            log.error(message);
            throw new ValidationException(message);
        }

        Comment comment = mapper.toComment(commentDto, user, event);
        comment = commentRepository.save(comment);
        log.info("Добавлен комментарий к событию eventId = {} пользователем userId = {}", eventId, userId);
        return mapper.toDto(comment);
    }

    // Удаление своего комментария
    @Override
    public void deleteCommentUser(long userId, long commentId) {
        Comment comment = getCommentEntity(commentId);

        if (comment.getUser().getId() != userId) {
            String message = "Комментарий может быть удален только его создателем. Пользователь userId = " + userId
                    + " не является создателем комментария commentId = " + commentId;
            log.error(message);
            throw new ValidationException(message);
        }

        commentRepository.deleteById(commentId);
        log.info("Пользователь userId = {} удалил свой комментарий commentId = {}", userId, commentId);
    }

    @Override
    public void deleteCommentAdmin(long commentId) {

    }

    @Override
    public Collection<CommentResponseDto> getEventComments(long eventId, int from, int size) {
        return null;
    }

    @Override
    public Comment getCommentEntity(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            String message = "Комментарий commentId = " + commentId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        });
    }
}
