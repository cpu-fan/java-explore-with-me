package ru.practicum.ewm.service.request;

import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.dto.request.ParticipationRequestUpdReqDto;
import ru.practicum.ewm.dto.request.ParticipationRequestUpdRespDto;
import ru.practicum.ewm.model.request.ParticipationRequest;

import java.util.Collection;

public interface ParticipationRequestService {

    // Private: Запросы на участие
    Collection<ParticipationRequestRespDto> getUserRequests(long userId);

    ParticipationRequestRespDto addRequest(long userId, long eventId);

    ParticipationRequestRespDto cancelRequest(long userId, long requestId);

    ParticipationRequest getRequestEntity(long requestId);

    // Private: События - Изменение статуса (подтверждена, отменена) заявок на участие
    // в событии текущего пользователя
    ParticipationRequestUpdRespDto updateRequest(long userId, long eventId, ParticipationRequestUpdReqDto requestDto);
}
