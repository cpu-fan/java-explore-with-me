package ru.practicum.ewm.stats.server.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class StatsServiceErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Валидация не пройдена: " + errors);
        return ResponseEntity.badRequest().body(Map.of("error", errors));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentNotValidException(final MissingServletRequestParameterException e) {
        String errors = Optional.ofNullable(e.getMessage()).orElse("Не передан обязательный параметр");
        log.error("Валидация не пройдена: " + errors);
        return ResponseEntity.badRequest().body(Map.of("error", errors));
    }
}
