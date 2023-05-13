package ru.practicum.ewm.controller.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.service.request.ParticipationRequestService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class ParticipationRequestPrivateController {

    private final ParticipationRequestService requestService;

    @GetMapping
    public Collection<ParticipationRequestRespDto> addUserRequests(@PathVariable long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestRespDto addRequest(@PathVariable long userId,
                                                  @RequestParam long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestRespDto cancelRequest(@PathVariable long userId,
                                                     @PathVariable long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
