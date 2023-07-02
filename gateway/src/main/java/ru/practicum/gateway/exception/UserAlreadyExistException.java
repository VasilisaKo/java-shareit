package ru.practicum.gateway.exception;

import lombok.Generated;

@Generated
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String s) {
        super(s);
    }
}
