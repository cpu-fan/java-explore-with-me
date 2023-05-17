package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.category.CategoryResponseDto;
import ru.practicum.ewm.dto.user.UserShortResponseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto implements Comparable<EventShortDto> {

    private long id;

    private String title;

    private String annotation;

    private String description;

    CategoryResponseDto category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortResponseDto initiator;

    private long confirmedRequests;

    private boolean paid;

    private long views;

    @Override
    public int compareTo(EventShortDto o) {
        return Long.compare(this.views, o.views);
    }
}
