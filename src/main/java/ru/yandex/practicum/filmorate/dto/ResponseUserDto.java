package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ResponseUserDto(
        int id,
        String email,
        String login,
        String name,
        LocalDate birthday
) {
}
