package ru.practicum.ewm.stats.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServerServiceImpl implements StatsServerService {

    private final HitRepository hitRepository;

    private final HitMapper hitMapper;

    @Override
    public void saveHit(HitRequestDto hitRequestDto) {
        Hit hit = hitMapper.toHit(hitRequestDto);
        hitRepository.save(hit);
    }

    @Override
    public List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, String uris, boolean unique) {
        List<String> uriList = Collections.emptyList();
        if (uris != null) {
            uriList = List.of(uris.split(","));
        }
        return hitRepository.findHits(start, end, uriList, unique);
    }
}
