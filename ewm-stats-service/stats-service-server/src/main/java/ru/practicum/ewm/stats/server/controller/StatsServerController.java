package ru.practicum.ewm.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.server.service.StatsServerServiceImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class StatsServerController {

    private final StatsServerServiceImpl statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> saveHit(@Valid @RequestBody HitRequestDto hitRequestDto) {
        statsService.saveHit(hitRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public List<HitResponseDto> getStats(
            @RequestParam @DateTimeFormat(pattern = HitRequestDto.DATE_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = HitRequestDto.DATE_PATTERN) LocalDateTime end,
            @RequestParam(required = false) String uris,
            @RequestParam(defaultValue = "false") boolean unique) {

        return statsService.getStats(start, end, uris, unique);
    }
}
