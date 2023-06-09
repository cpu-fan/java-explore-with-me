package ru.practicum.ewm.repository.event;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.model.event.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    boolean existsByCategoryId(long categoryId);

    boolean existsByIdAndInitiatorId(long eventId, long userId);

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    List<Event> findAllByInitiatorId(long userId, PageRequest page);

    Set<Event> findByIdIn(Collection<Long> eventIds);

    @NonNull
    Page<Event> findAll(@Nullable Predicate predicate, @Nullable Pageable pageable);
}
