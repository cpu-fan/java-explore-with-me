package ru.practicum.ewm.errorhandler;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private HttpStatus status;

    private String reason;

    private String message;

    private String timestamp;

    public ErrorResponse(HttpStatus status, String reason, String message, String timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
