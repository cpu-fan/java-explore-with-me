package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.dto.request.ParticipationRequestStatus;
import ru.practicum.ewm.dto.request.ParticipationRequestUpdReqDto;
import ru.practicum.ewm.dto.request.ParticipationRequestUpdRespDto;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.request.ParticipationRequestMapper;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.request.ParticipationRequest;
import ru.practicum.ewm.model.request.RequestStatus;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.request.ParticipationRequestRepository;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.model.event.EventState.PUBLISHED;
import static ru.practicum.ewm.model.request.RequestStatus.*;

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
        userService.getUserById(userId);
        log.info("Запрошен список запросов на события от пользователя id = {}", userId);
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(mapper::toDto)
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
            return mapper.toDto(request);
        } else {
            String message = "Создание заявки на участие в этом событии невозможно";
            log.error(message);
            throw new ConflictException(message);
        }
    }

    @Override
    public ParticipationRequestRespDto cancelRequest(long userId, long requestId) {
        userService.getUserById(userId);

        ParticipationRequest request = getRequestEntity(requestId);
        if (request.getStatus().equals(CONFIRMED)) {
            request.getEvent().setParticipantLimit(request.getEvent().getParticipantLimit() + 1);
        }
        request.setStatus(CANCELED);

        request = requestRepository.save(request);
        log.info("Отменен запрос requestId = {} на участие в событии eventId = {} пользователем userId = {}",
                requestId, request.getEvent().getId(), userId);
        return mapper.toDto(request);
    }

    @Override
    public ParticipationRequestUpdRespDto updateRequest(long userId, long eventId, ParticipationRequestUpdReqDto requestDto) {
        Event event = eventService.getUserEventEntity(userId, eventId);

        if (event.getParticipantLimit() == event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Достигнут лимит по заявкам на данное событие eventId = " + eventId);
        }

        List<ParticipationRequestRespDto> confirmed = new ArrayList<>();
        List<ParticipationRequestRespDto> rejected = new ArrayList<>();

        if (event.getParticipantLimit() == 0) {
            setRequests(confirmed, rejected, requestsNoLimit(requestDto, event));
        } else {
            setRequests(confirmed, rejected, requestsWithLimit(requestDto, event));
        }

        return new ParticipationRequestUpdRespDto(confirmed, rejected);
    }

    @Override
    public ParticipationRequest getRequestEntity(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос id = " + requestId + " не найден")
        );
    }

    private void setRequests(List<ParticipationRequestRespDto> confirmed,
                             List<ParticipationRequestRespDto> rejected,
                             List<ParticipationRequestRespDto> requests) {
        requests.forEach(request -> {
            if (request.getStatus().equals(CONFIRMED)) {
                confirmed.add(request);
            } else {
                rejected.add(request);
            }
        });
    }

    private List<ParticipationRequestRespDto> requestsNoLimit(ParticipationRequestUpdReqDto requestDto,
                                                              Event event) {
        return requestRepository.findByIdIn(requestDto.getRequestIds())
                .stream()
                .peek(request -> {
                    if (request.getStatus().equals(PENDING)) {

                        if (requestDto.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                            request.setStatus(CONFIRMED);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        } else {
                            request.setStatus(REJECTED);
                        }

                    } else {
                        throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
                    }
                })
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ParticipationRequestRespDto> requestsWithLimit(ParticipationRequestUpdReqDto statusUpdateRequest,
                                                                Event event) {
        return requestRepository.findByIdIn(statusUpdateRequest.getRequestIds())
                .stream()
                .peek(request -> {
                    if (request.getStatus().equals(PENDING)) {

                        if (statusUpdateRequest.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                            long limit = event.getParticipantLimit() - event.getConfirmedRequests();

                            if (limit > 0) {
                                request.setStatus(CONFIRMED);
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                            } else {
                                request.setStatus(REJECTED);
                            }

                        } else {
                            request.setStatus(REJECTED);
                        }

                    } else {
                        throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
                    }
                })
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
