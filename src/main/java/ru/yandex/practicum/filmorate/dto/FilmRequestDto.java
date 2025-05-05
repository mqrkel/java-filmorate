package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.Create;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;
import ru.yandex.practicum.filmorate.validator.Update;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record FilmRequestDto(

        @Null(groups = Create.class) @NotNull(groups = Update.class)
        Integer id,

        @NotEmpty(message = "Название не может быть пустым")
        String name,

        @Length(max = 200, message = "Максимальная длина описания — 200 символов")
        String description,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @ReleaseDate
        LocalDate releaseDate,

        @Positive(message = "Продолжительность должна быть положительным числом")
        Integer duration,

        Mpa mpa,

        Set<Genre> genres
) {
}
