package ru.practicum.ewm.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.model.request.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Jacksonized
public class ParticipationRequestRespDto {

    private long id;

    private long event;

    private long requester;

    private LocalDateTime created;

    private RequestStatus status;
}
