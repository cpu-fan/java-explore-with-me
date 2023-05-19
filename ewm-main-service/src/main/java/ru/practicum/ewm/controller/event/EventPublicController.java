package ru.practicum.ewm.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.dto.event.EventSearchFilters;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    private final StatsService statsService;

    @GetMapping("/{id}")
    public EventResponseDto getEvent(@PathVariable long id,
                                     HttpServletRequest servlet) {
        statsService.saveHit(servlet);
        return eventService.getEvent(id);
    }

    @GetMapping
    public Collection<EventShortDto> getEvents(EventSearchFilters filters,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest servlet) {
        statsService.saveHit(servlet);
        return eventService.searchEventsPublic(filters, from, size);
    }
}
