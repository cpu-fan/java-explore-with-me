package ru.practicum.ewm.dto.event;import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.ewm.validator.CustomDateTimeConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@Jacksonized
public class EventRequestDto {

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private long category;

    @NotNull
    @CustomDateTimeConstraint
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @Value("${paid:false}")
    private boolean paid;

    @PositiveOrZero
    @Value("${participantLimit:0}")
    private long participantLimit;

    @Value("${requestModeration:true}")
    private boolean requestModeration;
}