package ru.yandex.practicum.filmorate.exception;

public class FilmCreationException extends RuntimeException {
    public FilmCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}