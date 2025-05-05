package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record UserResponseDto(
        int id,
        String email,
        String login,
        String name,
        LocalDate birthday,
        Set<Integer> friendshipIds,
        Set<Integer> likedFilms
) {
}