package ru.yandex.practicum.filmorate.exception;

public class DataMappingException extends RuntimeException {
    public DataMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}