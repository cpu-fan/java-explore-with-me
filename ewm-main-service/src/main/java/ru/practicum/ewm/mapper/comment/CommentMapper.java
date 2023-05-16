package ru.practicum.ewm.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.comment.CommentRequestDto;
import ru.practicum.ewm.dto.comment.CommentResponseDto;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

//    @Mapping(source = "user", target = "user")
//    @Mapping(source = "event", target = "event")
    @Mapping(target = "id", ignore = true)
    Comment toComment(CommentRequestDto commentDto, User user, Event event);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "user", target = "commentator")
    CommentResponseDto toDto(Comment comment);
}
