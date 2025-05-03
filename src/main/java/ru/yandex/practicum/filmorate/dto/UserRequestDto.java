package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import ru.yandex.practicum.filmorate.validator.Create;
import ru.yandex.practicum.filmorate.validator.NoSpaces;
import ru.yandex.practicum.filmorate.validator.Update;

import java.time.LocalDate;
import java.util.Set;

public record UserRequestDto(

        @Null(groups = Create.class) @NotNull(groups = Update.class)
        Integer id,

        @Email(message = "Неверный формат email")
        @NotBlank(message = "Email не должен быть пустым")
        String email,

        @NotBlank(message = "Login не должен быть пустым")
        @NoSpaces
        String login,

        String name,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthday,

        Set<Integer> friendshipIds,
        Set<Integer> likedFilms
) {
}