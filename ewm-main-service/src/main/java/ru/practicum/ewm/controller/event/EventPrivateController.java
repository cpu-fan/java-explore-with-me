package ru.practicum.ewm.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdateDto;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.dto.request.ParticipationRequestUpdReqDto;
import ru.practicum.ewm.dto.request.ParticipationRequestUpdRespDto;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.request.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {

    private final EventService eventService;

    private final ParticipationRequestService participationRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto addEvent(@PathVariable long userId,
                                     @Valid @RequestBody EventRequestDto event) {
        return eventService.addEvent(userId, event);
    }

    @GetMapping("/{eventId}")
    public EventResponseDto getUserEvent(@PathVariable long userId,
                                         @PathVariable long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @GetMapping
    public Collection<EventShortDto> getUserEvents(@PathVariable long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventResponseDto updateEvent(@PathVariable @Positive long userId,
                                        @PathVariable @Positive long eventId,
                                        @Valid @RequestBody EventUpdateDto requestDto) {
        return eventService.updateEvent(requestDto, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestRespDto> getEventRequests(@PathVariable @Positive long userId,
                                                                    @PathVariable @Positive long eventId) {
        return eventService.getUserEventsRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public ParticipationRequestUpdRespDto updateEventRequests(@PathVariable @Positive Long userId,
                                                              @PathVariable @Positive Long eventId,
                                                              @Valid @RequestBody ParticipationRequestUpdReqDto requestDto) {
        return participationRequestService.updateRequest(userId, eventId, requestDto);
    }
}
