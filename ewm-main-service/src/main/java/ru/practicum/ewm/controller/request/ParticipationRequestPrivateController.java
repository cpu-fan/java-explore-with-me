package ru.practicum.ewm.controller.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.service.request.ParticipationRequestService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
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

    // TODO: здесь сделать Patch
}
