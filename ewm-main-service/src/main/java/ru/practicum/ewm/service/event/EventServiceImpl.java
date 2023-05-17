package ru.practicum.ewm.service.event;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.request.ParticipationRequestRespDto;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.event.EventMapper;
import ru.practicum.ewm.mapper.request.ParticipationRequestMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.QEvent;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.request.ParticipationRequestRepository;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.stats.StatsService;
import ru.practicum.ewm.service.user.UserService;
import ru.practicum.ewm.stats.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.dto.event.EventSort.VIEWS;
import static ru.practicum.ewm.model.event.EventState.*;
import static ru.practicum.ewm.model.event.StateActionAdmin.PUBLISH_EVENT;
import static ru.practicum.ewm.model.event.StateActionAdmin.REJECT_EVENT;
import static ru.practicum.ewm.model.event.StateActionUser.CANCEL_REVIEW;
import static ru.practicum.ewm.model.event.StateActionUser.SEND_TO_REVIEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final UserService userService;

    private final CategoryService categoryService;

    private final EventMapper mapper;

    private final EventRepository eventRepository;

    private final StatsService statsService;

    private final ParticipationRequestMapper participationReqMapper;

    private final ParticipationRequestRepository requestRepository;

    @Override
    public EventResponseDto getEvent(long eventId) {
        Event event = getEventEntity(eventId);
        log.info("Запрошено событие eventId = {}, title = {}", eventId, event.getTitle());
        return getEventsResponseDto(List.of(event)).get(0);
    }

    @Override
    public Collection<EventShortDto> searchEventsPublic(EventSearchFilters filters, int from, int size) {

        if (filters.getOnlyAvailable() == null) {
            filters.setOnlyAvailable(Boolean.FALSE);
        }

        BooleanBuilder queryBuilder = queryBuilder(filters);
        if (filters.getRangeStart() == null && filters.getRangeEnd() == null) {
            queryBuilder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }

        List<Event> events = eventRepository.findAll(queryBuilder,
                PageRequest.of(from, size, Sort.by("eventDate").ascending())).toList();
        List<EventShortDto> eventsDto = getEventsShortDto(events);

        if (filters.getSort() != null && filters.getSort().equals(VIEWS)) {
            eventsDto.sort(EventShortDto::compareTo);
        }

        return eventsDto;
    }

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
    public Collection<EventShortDto> getUserEvents(long userId, int from, int size) {
        userService.getUserById(userId);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size));
        log.info("Запрошены события пользователя userId = {}", userId);
        if (!events.isEmpty()) {
            return getEventsShortDto(events);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public EventResponseDto getUserEvent(long userId, long eventId) {
        Event event = getUserEventEntity(userId, eventId);
        log.info("Запрошено событие eventId = {} пользователя userId = {}", event, userId);
        return getEventsResponseDto(List.of(event)).get(0);
    }

    @Override
    public EventResponseDto updateEvent(EventUpdateDto eventDto, long userId, long eventId) {
        Event event = getUserEventEntity(userId, eventId);

        if (!event.getState().equals(PUBLISHED)) {
            event = mapper.updateEventUser(eventDto, event);

            if (eventDto.getStateAction() != null) {

                if (eventDto.getStateAction().equals(SEND_TO_REVIEW)) {
                    event.setState(PENDING);
                }
                if (eventDto.getStateAction().equals(CANCEL_REVIEW)) {
                    event.setState(CANCELED);
                }
            }

            if (eventDto.getCategory() != null) {
                Category category = categoryService.getEntityCategory(eventDto.getCategory());
                event.setCategory(category);
            }

            event = eventRepository.save(event);
            log.info("Обновлена информация о событии eventId = {}", eventId);
            return mapper.toDto(event);
        }
        throw new ConflictException("Текущий статус state = " + event.getState() + " не позволяет обновить событие");
    }

    @Override
    public Collection<ParticipationRequestRespDto> getUserEventsRequests(long userId, long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException("Событие id = " + eventId + " не найдено");
        }
        log.info("Запрошена информация о запросах на участие в событии eventId = {} пользователя userId = {}", eventId, userId);
        return requestRepository.findByEventId(eventId).stream()
                .map(participationReqMapper::toDto)
                .collect(Collectors.toList());
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
        List<Event> events = eventRepository.findAll(queryBuilder(filters), PageRequest.of(from / size, size)).toList();
        log.info("Поиск событий администратором с параметрами запроса filters = {}", filters);
        return getEventsResponseDto(events);
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

            event = mapper.updateEventAdmin(eventDto, event);
            event = eventRepository.save(event);
            log.info("Обновлена информация о событии eventId = {}", eventId);
            return mapper.toDto(event);
        }
        throw new ConflictException("Текущий статус state = " + event.getState() + " не позволяет обновить событие");
    }

    @NonNull
    private BooleanBuilder queryBuilder(@NonNull EventSearchFilters filters) {
        BooleanBuilder queryBuilder = new BooleanBuilder();

        if (filters.getText() != null) {
            queryBuilder.and(QEvent.event.annotation.likeIgnoreCase(filters.getText())
                    .or(QEvent.event.description.likeIgnoreCase(filters.getText())));
        }

        if (filters.getUsers() != null && !filters.getUsers().isEmpty()) {
            queryBuilder.and(QEvent.event.initiator.id.in(filters.getUsers()));
        }

        if (filters.getStates() != null && !filters.getStates().isEmpty()) {
            queryBuilder.and(QEvent.event.state.in(filters.getStates()));
        }

        if (filters.getCategories() != null && !filters.getCategories().isEmpty()) {
            queryBuilder.and(QEvent.event.category.id.in(filters.getCategories()));
        }

        if (filters.getPaid() != null) {
            queryBuilder.and(QEvent.event.paid.eq(filters.getPaid()));
        }

        if (filters.getRangeStart() != null) {
            queryBuilder.and(QEvent.event.eventDate.after(filters.getRangeStart()));
        }

        if (filters.getRangeEnd() != null) {
            queryBuilder.and(QEvent.event.eventDate.before(filters.getRangeEnd()));
        }

        if (filters.getOnlyAvailable() != null) {
            queryBuilder.and(QEvent.event.participantLimit.eq(0L)
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests)));
        }

        return queryBuilder;
    }

    private List<EventShortDto> getEventsShortDto(List<Event> eventList) {
        if (!eventList.isEmpty()) {

            Map<Long, Long> stats = getStatMap(eventList);

            if (stats != null && !stats.isEmpty()) {

                return eventList.stream()
                        .map(event -> stats.containsKey(event.getId()) ?
                                mapper.toShortDtoWithViews(event, stats.get(event.getId())) :
                                mapper.toShortDto(event))
                        .collect(Collectors.toList());
            }
        }
        return eventList.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    private List<EventResponseDto> getEventsResponseDto(List<Event> eventList) {
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
