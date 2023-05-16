package ru.practicum.ewm.controller.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentResponseDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentPublicController {

    @GetMapping
    public Collection<CommentResponseDto> getEventComments(@PathVariable long eventId,
                                                           @RequestParam @PositiveOrZero int from,
                                                           @RequestParam @Positive int size) {
        return null;
    }
}
