package ru.yandex.practicum.filmorate.exception;

public class CreationException extends RuntimeException {
    public CreationException(String message, Throwable cause) {
        super(message, cause);
    }
}