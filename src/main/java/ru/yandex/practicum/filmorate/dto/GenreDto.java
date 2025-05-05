package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;

@Builder
public record GenreDto(
        Integer id,
        String name) {
}
