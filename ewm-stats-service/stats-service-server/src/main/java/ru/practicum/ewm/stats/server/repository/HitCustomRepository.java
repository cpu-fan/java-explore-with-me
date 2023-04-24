package ru.practicum.ewm.stats.server.repository;

import ru.practicum.ewm.stats.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitCustomRepository {
    List<HitResponseDto> findHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
