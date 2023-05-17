package ru.practicum.ewm.controller.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentResponseDto;
import ru.practicum.ewm.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping
    public Collection<CommentResponseDto> getEventComments(@PathVariable long eventId,
                                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return commentService.getEventComments(eventId, from, size);
    }
}
