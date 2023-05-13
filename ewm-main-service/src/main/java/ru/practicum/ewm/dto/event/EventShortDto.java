package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.category.CategoryResponseDto;
import ru.practicum.ewm.dto.user.UserShortResponseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private long id;

    private String title;

    private String annotation;

    private String description;

    CategoryResponseDto category;

    private LocalDateTime eventDate;

    private UserShortResponseDto initiator;

    private long confirmedRequests;

    private boolean paid;

    private long views;
}
