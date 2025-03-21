package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ResponseFilmDto(
        Integer id,
        String name,
        String description,
        LocalDate releaseDate,
        int duration) {
}

