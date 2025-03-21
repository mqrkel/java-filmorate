package ru.yandex.practicum.filmorate.exception;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}