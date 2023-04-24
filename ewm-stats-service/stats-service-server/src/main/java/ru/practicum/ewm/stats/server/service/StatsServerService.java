package ru.practicum.ewm.stats.server.service;

import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerService {

    void saveHit(HitRequestDto hitRequestDto);

    List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, String uris, boolean unique);
}
