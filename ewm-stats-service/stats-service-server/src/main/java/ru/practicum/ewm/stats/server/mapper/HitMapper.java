package ru.practicum.ewm.stats.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.server.model.Hit;

@Component
public class HitMapper {

    public Hit toHit(HitRequestDto hitRequestDto) {
        return Hit.builder()
                .id(0)
                .app(hitRequestDto.getApp())
                .ip(hitRequestDto.getIp())
                .uri(hitRequestDto.getUri())
                .timestamp(hitRequestDto.getTimestamp())
                .build();
    }

    public HitResponseDto toHitResponseDto(Hit hit, long hits) {
        return new HitResponseDto(hit.getApp(), hit.getUri(), hits);
    }
}
