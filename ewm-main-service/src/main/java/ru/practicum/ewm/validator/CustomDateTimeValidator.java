package ru.practicum.ewm.validator;

import ru.practicum.ewm.errorhandler.exceptions.DateTimeValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CustomDateTimeValidator implements ConstraintValidator<CustomDateTimeConstraint, LocalDateTime> {

    private int value;

    private String message;

    @Override
    public void initialize(CustomDateTimeConstraint constraintAnnotation) {
        value = constraintAnnotation.value();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext cxt) {
        if (dateTime == null) {
            return true;
        }
        if (dateTime.minusHours(value).isBefore(LocalDateTime.now())) {
            throw new DateTimeValidationException(message);
        }
        return true;
    }
}
