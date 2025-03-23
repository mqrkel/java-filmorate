package ru.yandex.practicum.filmorate.exception;

public class UpdateException extends RuntimeException {
    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}