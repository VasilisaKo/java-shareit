package ru.practicum.gateway.exception;

public class CannotBookItemException extends RuntimeException {
    public CannotBookItemException(String s) {
        super(s);
    }
}
