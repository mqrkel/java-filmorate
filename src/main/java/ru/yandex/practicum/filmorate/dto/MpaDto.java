package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;

@Builder
public record MpaDto(
        Integer id,
        String name
) {
}