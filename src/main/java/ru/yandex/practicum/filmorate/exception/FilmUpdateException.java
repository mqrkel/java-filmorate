package ru.yandex.practicum.filmorate.exception;

public class FilmUpdateException extends RuntimeException {
    public FilmUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}