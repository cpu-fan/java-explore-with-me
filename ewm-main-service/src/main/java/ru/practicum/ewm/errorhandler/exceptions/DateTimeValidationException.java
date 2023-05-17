package ru.practicum.ewm.errorhandler.exceptions;

import javax.validation.ConstraintDeclarationException;

public class DateTimeValidationException extends ConstraintDeclarationException {
    public DateTimeValidationException(String message) {
        super(message);
    }
}
