package ru.practicum.ewm.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventAdminRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.dto.event.EventSearchFilters;
import ru.practicum.ewm.service.event.EventService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public Collection<EventResponseDto> getEvents(EventSearchFilters filters,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return eventService.searchEventsAdmin(filters, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventResponseDto updateEventAdmin(@PathVariable long eventId,
                                             @RequestBody @Valid EventAdminRequestDto eventDto) {
        return eventService.updateEventAdmin(eventId, eventDto);
    }
}
