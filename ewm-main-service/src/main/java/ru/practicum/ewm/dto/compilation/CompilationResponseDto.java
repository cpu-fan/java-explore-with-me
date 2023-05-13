package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {

    private long id;

    private String title;

    private Set<EventShortDto> events;

    private boolean pinned;
}
