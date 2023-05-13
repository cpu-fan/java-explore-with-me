package ru.practicum.ewm.repository.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.event.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategoryId(long categoryId);

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    List<Event> findByInitiatorId(Long userId, PageRequest pageRequest);

    Set<Event> findByIdIn(Collection<Long> eventIds);
}
