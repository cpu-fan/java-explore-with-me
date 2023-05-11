package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.dto.category.CategoryResponseDto;
import ru.practicum.ewm.dto.user.UserShortResponseDto;
import ru.practicum.ewm.model.event.EventState;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@Jacksonized
public class EventResponseDto {

    private long id;

    private String title;

    private String annotation;

    private String description;

    private CategoryResponseDto category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private UserShortResponseDto initiator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private boolean paid;

    private long participantLimit;

    private long confirmedRequests;

    private boolean requestModeration;

    private EventState state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private long views;
}
