package ru.practicum.ewm.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
public class StatsClient {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(HitRequestDto.DATE_PATTERN);
    private static final String HIT_PATH = "/hit";
    private static final String GET_STATS_PATH = "/stats";

    private final WebClient webClient;

    public StatsClient(@Value("${stats-server.url}") String url) {
        webClient = WebClient.create(url);
    }

    public Mono<Void> saveHit(HitRequestDto hitRequestDto) {
        return webClient.post()
                .uri(HIT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hitRequestDto)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<HitResponseDto> getStats(LocalDateTime start,
                                         LocalDateTime end,
                                         Collection<String> uris,
                                         boolean unique) {
        return webClient.get()
                .uri(u -> u.path(GET_STATS_PATH)
                        .queryParam("start", start.format(DATE_TIME_FORMAT))
                        .queryParam("end", end.format(DATE_TIME_FORMAT))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(HitResponseDto.class);
    }
}
