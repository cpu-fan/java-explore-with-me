package ru.practicum.ewm.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.model.event.StateActionUser;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@Jacksonized
public class EventUpdateDto {

    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 20, max = 7000)
    private String description;

    private Long category;

    // valid date
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateActionUser stateAction;
}
