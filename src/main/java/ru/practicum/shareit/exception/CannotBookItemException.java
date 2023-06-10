package ru.practicum.shareit.exception;

public class CannotBookItemException extends RuntimeException {
    public CannotBookItemException(String s) {
        super(s);
    }
}
