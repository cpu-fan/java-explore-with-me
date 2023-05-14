package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.EventAdminRequestDto;
import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.dto.event.EventSearchFilters;
import ru.practicum.ewm.model.event.Event;

import java.util.Collection;
import java.util.Set;

public interface EventService {

    EventResponseDto addEvent(long userId, EventRequestDto event);

    EventResponseDto getUserEvent(long userId, long eventId);

    Event getUserEventEntity(long userId, long eventId);

    Collection<EventResponseDto> getUserEvents(long userId, int from, int size);

    Event getEventEntity(long eventId);

    Set<Event> getEventEntities(Collection<Long> eventIds);

    // Admin
    Collection<EventResponseDto> searchEventsAdmin(EventSearchFilters filters, int from, int size);

    EventResponseDto updateEventAdmin(long eventId, EventAdminRequestDto eventDto);
}
