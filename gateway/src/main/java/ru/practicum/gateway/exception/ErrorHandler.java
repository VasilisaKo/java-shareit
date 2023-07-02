package ru.practicum.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ru.practicum.gateway.exception.ErrorResponse handle(final NotFoundException e) {
        log.warn("Ошибка NotFoundException {}", e.getMessage());
        return new ru.practicum.gateway.exception.ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ru.practicum.gateway.exception.ErrorResponse handle(final ValidationException e) {
        log.warn("Ошибка ValidationException {}", e.getMessage());
        return new ru.practicum.gateway.exception.ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ru.practicum.gateway.exception.ErrorResponse handle(final UserAlreadyExistException e) {
        log.warn("Исключение UserAlreadyExistException {}", e.getMessage());
        return new ru.practicum.gateway.exception.ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ru.practicum.gateway.exception.ErrorResponse handle(final CannotBookItemException e) {
        log.warn("Исключение CannotBookItemException {}", e.getMessage());
        return new ru.practicum.gateway.exception.ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ru.practicum.gateway.exception.ErrorResponse handle(Exception e) {
        log.warn("Ошибка MethodArgumentTypeMismatchException {}", e.getMessage());
        String exceptionType = "Unknown state: UNSUPPORTED_STATUS";
        String errorMessage = e.getMessage();
        return new ru.practicum.gateway.exception.ErrorResponse(exceptionType, errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ru.practicum.gateway.exception.ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.warn("Ошибка ConstraintViolationException {}", e.getMessage());
        return new ru.practicum.gateway.exception.ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ru.practicum.gateway.exception.ErrorResponse handleSqlException(final DataIntegrityViolationException e) {
        log.warn("Ошибка DataIntegrityViolationException {}", e.getMessage());
        return new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
    }
}
