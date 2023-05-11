package ru.practicum.ewm.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.user.UserService;

import java.util.Collection;

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
        return mapper.toResponse(event);
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
        return mapper.toResponse(event);
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
}
