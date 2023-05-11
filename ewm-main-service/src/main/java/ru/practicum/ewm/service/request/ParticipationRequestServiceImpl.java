package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.mapper.request.ParticipationRequestMapper;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.request.ParticipationRequest;
import ru.practicum.ewm.model.request.RequestStatus;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.request.ParticipationRequestRepository;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.user.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.ewm.model.event.EventState.PUBLISHED;
import static ru.practicum.ewm.model.request.RequestStatus.CONFIRMED;
import static ru.practicum.ewm.model.request.RequestStatus.PENDING;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final EventService eventService;

    private final UserService userService;

    private final ParticipationRequestRepository requestRepository;

    private final ParticipationRequestMapper mapper;

    @Override
    public Collection<ParticipationRequestRespDto> getUserRequests(long userId) {
        User user = userService.getUserById(userId);
        log.info("Запрошен список запросов на события от пользователя id = {}", userId);
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public ParticipationRequestRespDto addRequest(long userId, long eventId) {
        Event event = eventService.getEventEntity(eventId);
        User user = userService.getUserById(userId);

        if ((event.getInitiator().getId() != userId)
                && event.getState().equals(PUBLISHED)
                && event.getParticipantLimit() > 0) {

            RequestStatus status;
            if (event.isRequestModeration()) {
                status = PENDING;
            } else {
                status = CONFIRMED;
                event.setParticipantLimit(event.getParticipantLimit() - 1);
            }

            ParticipationRequest request = ParticipationRequest.builder()
                    .requester(user)
                    .event(event)
                    .status(status)
                    .build();

            request = requestRepository.save(request);
            log.info("Добавлен новый запрос requestId = {} на событие eventId = {} пользователем userId = {}",
                    request.getId(), eventId, userId);
            return mapper.toResponse(request);
        } else {
            String message = "Создание заявки на участие в этом событии невозможно";
            log.error(message);
            throw new ConflictException(message);
        }
    }

    @Override
    public ParticipationRequestRespDto cancelRequest(long userId, long requestId) {
        return null;
    }
}
