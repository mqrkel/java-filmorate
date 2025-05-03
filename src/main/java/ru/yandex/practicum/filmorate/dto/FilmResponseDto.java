package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record FilmResponseDto(
        int id,
        String name,
        String description,
        LocalDate releaseDate,
        int duration,
        Set<Integer> likeIds,
        MpaDto mpa,
        Set<GenreDto> genres
) {
}