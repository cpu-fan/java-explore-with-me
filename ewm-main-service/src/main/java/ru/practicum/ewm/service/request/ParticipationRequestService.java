package ru.practicum.ewm.service.request;

import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;

import java.util.Collection;

public interface ParticipationRequestService {

    Collection<ParticipationRequestRespDto> getUserRequests(long userId);

    ParticipationRequestRespDto addRequest(long userId, long eventId);

    ParticipationRequestRespDto cancelRequest(long userId, long requestId);


//    ParticipationStatusUpdateResponse updateRequestStatus(
//            long userId, long eventId, ParticipationStatusUpdateRequest updateRequest
//    );
}
