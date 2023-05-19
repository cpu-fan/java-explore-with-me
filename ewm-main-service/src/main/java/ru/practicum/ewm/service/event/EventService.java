package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.model.event.Event;

import java.util.Collection;
import java.util.Set;

public interface EventService {

    // Public
    EventResponseDto getEvent(long eventId);

    Collection<EventShortDto> searchEventsPublic(EventSearchFilters filters, int from, int size);

    // Private
    EventResponseDto addEvent(long userId, EventRequestDto event);

    EventResponseDto getUserEvent(long userId, long eventId);

    Event getUserEventEntity(long userId, long eventId);

    Collection<EventShortDto> getUserEvents(long userId, int from, int size);

    EventResponseDto updateEvent(EventUpdateDto eventDto, long userId, long eventId);

    Event getEventEntity(long eventId);

    Set<Event> getEventEntities(Collection<Long> eventIds);

    boolean existById(long eventId);

    // Admin
    Collection<EventResponseDto> searchEventsAdmin(EventSearchFilters filters, int from, int size);

    EventResponseDto updateEventAdmin(long eventId, EventAdminRequestDto eventDto);

    Collection<ParticipationRequestRespDto> getUserEventsRequests(long userId, long eventId);
}
