package ru.yandex.practicum.filmorate.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String error,
        int status,
        LocalDateTime timestamp
) {
}