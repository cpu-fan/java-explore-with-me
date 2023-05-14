package ru.practicum.ewm.service.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.HitResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private static final String APP = "ewm-service";
    private final StatsClient statsClient;

    @Override
    public Flux<HitResponseDto> getStats(LocalDateTime start, Collection<Long> eventIds) {
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = eventIds.stream()
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());
        return statsClient.getStats(start, end, uris, true);
    }

    @Override
    public void saveHit(HttpServletRequest request) {

    }
}
