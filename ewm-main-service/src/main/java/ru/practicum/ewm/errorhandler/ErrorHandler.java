package ru.practicum.ewm.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.errorhandler.exceptions.ConflictException;
import ru.practicum.ewm.errorhandler.exceptions.DateTimeValidationException;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss:mm");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExistsException(final DataIntegrityViolationException e) {
        log.error("Нарушение целостности данных: ", e);
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                "Нарушение целостности данных",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.error("Нарушение целостности данных: ", e);
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                "Для запрошенной операции условия не соблюдены",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDateTimeValidationException(final DateTimeValidationException e) {
        log.error("Ошибка валидации даты: ", e);
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                "Ошибка валидации даты",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final MethodArgumentNotValidException e) {
        String reason = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).toString().split(";")[0];

        log.error("Валидация не пройдена: " + message);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                reason,
                message,
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.error("Валидация не пройдена: " + e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error("Валидация не пройдена: " + e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                e.toString(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка: ", e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }
}
