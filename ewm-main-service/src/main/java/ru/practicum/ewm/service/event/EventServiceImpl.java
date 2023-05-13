package ru.practicum.ewm.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventAdminRequestDto;
import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.user.UserService;

import java.util.Collection;
import java.util.Set;

import static ru.practicum.ewm.model.event.EventState.*;
import static ru.practicum.ewm.model.event.StateAction.PUBLISH_EVENT;
import static ru.practicum.ewm.model.event.StateAction.REJECT_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventMapper mapper;
    private final EventRepository eventRepository;

    @Override
    public EventResponseDto addEvent(long userId, EventRequestDto eventDto) {
        Category category = categoryService.getEntityCategory(eventDto.getCategory());
        User user = userService.getUserById(userId);

        Event event = mapper.toEntity(eventDto);
        event.setCategory(category);
        event.setInitiator(user);

        event = eventRepository.save(event);
        log.info("Добавлена новое событие: id = {}, title = {}", event.getId(), event.getTitle());
        return mapper.toDto(event);
    }

    @Override
    public Collection<EventResponseDto> getUserEvents(long userId, int from, int size) {
        userService.getUserById(userId);
        // TODO: доделать
        return null;
    }

    @Override
    public EventResponseDto getUserEvent(long userId, long eventId) {
        Event event = getUserEventEntity(userId, eventId);
        // TODO: тут наверно надо будет потом добавить обращение к запросам и просмотрам
        return mapper.toDto(event);
    }

    @Override
    public Event getUserEventEntity(long userId, long eventId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие id = " + eventId + " не найдено"));
    }

    @Override
    public Event getEventEntity(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие id = " + eventId + " не найдено"));
    }

    @Override
    public Set<Event> getEventEntities(Collection<Long> eventIds) {
        return eventRepository.findByIdIn(eventIds);
    }

    @Override
    public EventResponseDto updateEventAdmin(long eventId, EventAdminRequestDto eventDto) {
        Event event = getEventEntity(eventId);

        if (!event.getState().equals(PUBLISHED)) {

            if (eventDto.getStateAction() != null) {

                if (eventDto.getStateAction().equals(PUBLISH_EVENT) &&
                        event.getState().equals(PENDING)) {
                    event.setState(PUBLISHED);
                } else if (eventDto.getStateAction().equals(REJECT_EVENT)) {
                    event.setState(CANCELED);
                } else {
                    throw new ConflictException("Текущий статус state = " + event.getState() + " не позволяет обновить событие");
                }
            }

            event = mapper.updateAdmin(eventDto, event);
            event = eventRepository.save(event);
            return mapper.toDto(event);
        }

        throw new ConflictException("Текущий статус state = " + event.getState() + " не позволяет обновить событие");
    }
}
