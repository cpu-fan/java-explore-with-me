package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.stats.server.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long>, QuerydslPredicateExecutor<Hit>, HitCustomRepository {
}
