package ru.practicum.server.exception;

public class ErrorResponse {
    private final String error;
    private String message;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
