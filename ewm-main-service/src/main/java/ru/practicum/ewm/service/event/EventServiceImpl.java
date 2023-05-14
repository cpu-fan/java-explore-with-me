package ru.practicum.ewm.service.event;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventAdminRequestDto;
import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.dto.event.EventSearchFilters;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.QEvent;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.stats.StatsService;
import ru.practicum.ewm.service.user.UserService;
import ru.practicum.ewm.stats.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final StatsService statsService;

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
    public Collection<EventResponseDto> searchEventsAdmin(EventSearchFilters filters, int from, int size) {
        List<Event> events = eventRepository.findAll(queryBuilder(filters), PageRequest.of(from, size)).toList();
        return getEventResponseDto(events);
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

    @NonNull
    private BooleanBuilder queryBuilder(@NonNull EventSearchFilters filters) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filters.getText() != null) {
            builder.and(QEvent.event.annotation.likeIgnoreCase(filters.getText())
                    .or(QEvent.event.description.likeIgnoreCase(filters.getText())));
        }

        if (filters.getUsers() != null && !filters.getUsers().isEmpty()) {
            builder.and(QEvent.event.initiator.id.in(filters.getUsers()));
        }

        if (filters.getStates() != null && !filters.getStates().isEmpty()) {
            builder.and(QEvent.event.state.in(filters.getStates()));
        }

        if (filters.getCategories() != null && !filters.getCategories().isEmpty()) {
            builder.and(QEvent.event.category.id.in(filters.getCategories()));
        }

        if (filters.getPaid() != null) {
            builder.and(QEvent.event.paid.eq(filters.getPaid()));
        }

        if (filters.getRangeStart() != null) {
            builder.and(QEvent.event.eventDate.after(filters.getRangeStart()));
        }

        if (filters.getRangeEnd() != null) {
            builder.and(QEvent.event.eventDate.before(filters.getRangeEnd()));
        }

        if (filters.getOnlyAvailable() != null) {
            builder.and(QEvent.event.participantLimit.eq(0L)
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests)));
        }

        return builder;
    }

    private List<EventResponseDto> getEventResponseDto(List<Event> eventList) {
        if (!eventList.isEmpty()) {
            Map<Long, Long> stats = getStatMap(eventList);
            if (stats != null && !stats.isEmpty()) {
                return eventList.stream()
                        .map(event -> stats.containsKey(event.getId()) ?
                                mapper.toDtoWithViews(event, stats.get(event.getId())) :
                                mapper.toDto(event))
                        .collect(Collectors.toList());
            }
        }
        return eventList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private Map<Long, Long> getStatMap(List<Event> events) {
        LocalDateTime start = events.stream()
                .map(Event::getCreatedOn)
                .min(LocalDateTime::compareTo).orElse(LocalDateTime.MIN);

        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        return statsService.getStats(start, eventIds)
                .collect(Collectors.toMap(hits -> Long.valueOf(hits.getUri()
                        .substring(hits.getUri().length() - 1)), HitResponseDto::getHits))
                .block();
    }
}
