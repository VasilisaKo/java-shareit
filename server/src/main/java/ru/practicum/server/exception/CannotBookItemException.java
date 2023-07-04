package ru.practicum.server.exception;

public class CannotBookItemException extends RuntimeException {
    public CannotBookItemException(String s) {
        super(s);
    }
}
