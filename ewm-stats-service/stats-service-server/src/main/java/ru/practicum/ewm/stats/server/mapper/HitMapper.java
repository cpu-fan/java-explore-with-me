package ru.practicum.ewm.stats.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.server.model.App;
import ru.practicum.ewm.stats.server.model.Hit;

@Component
public class HitMapper {

    public Hit toHit(HitRequestDto hitRequestDto, App app) {
        return Hit.builder()
                .id(0)
                .app(app)
                .ip(hitRequestDto.getIp())
                .uri(hitRequestDto.getUri())
                .timestamp(hitRequestDto.getTimestamp())
                .build();
    }
}
