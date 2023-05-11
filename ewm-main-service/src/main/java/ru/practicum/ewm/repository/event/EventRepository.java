package ru.practicum.ewm.repository.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.event.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategoryId(long categoryId);

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    List<Event> findByInitiatorId(Long userId, PageRequest pageRequest);
}
