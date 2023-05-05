package ru.practicum.ewm.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
                e.getCause().getMessage(),
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

        log.error("Bad request: {}", message);
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
        String message = e.getMessage();
        log.error("Bad request: {}", message);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getCause().getMessage(),
                message,
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("Not found: ", e);
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
                e.getCause().getMessage(),
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }
}
