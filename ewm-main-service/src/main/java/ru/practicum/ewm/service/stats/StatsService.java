package ru.practicum.ewm.service.stats;

import reactor.core.publisher.Flux;
import ru.practicum.ewm.stats.dto.HitResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {

    Flux<HitResponseDto> getStats(LocalDateTime start, Collection<Long> eventIds);

    void saveHit(HttpServletRequest request);
}
