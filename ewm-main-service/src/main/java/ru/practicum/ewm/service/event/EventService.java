package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.model.event.Event;

import java.util.Collection;

public interface EventService {

    EventResponseDto addEvent(long userId, EventRequestDto event);

    EventResponseDto getUserEvent(long userId, long eventId);

    Event getUserEventEntity(long userId, long eventId);

    Collection<EventResponseDto> getUserEvents(long userId, int from, int size);
}
