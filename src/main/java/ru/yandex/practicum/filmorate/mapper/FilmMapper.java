package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;

@UtilityClass
public final class FilmMapper {

    public static ResponseFilmDto convertToResponseFilmDto(Film updatedFilm) {
        return ResponseFilmDto.builder()
                .id(updatedFilm.getId())
                .name(updatedFilm.getName())
                .description(updatedFilm.getDescription())
                .releaseDate(updatedFilm.getReleaseDate())
                .duration((int) updatedFilm.getDuration().toMinutes())
                .likes(updatedFilm.getLikes())
                .build();
    }

    public static Film convertToFilm(RequestFilmDto filmDto) {
        return Film.builder()
                .name(filmDto.name())
                .description(filmDto.description())
                .releaseDate(filmDto.releaseDate())
                .duration(Duration.ofMinutes(filmDto.duration()))
                .build();
    }

    public static Film convertToFilm(RequestFilmWithIdDto filmDto) {
        return Film.builder()
                .id(filmDto.id())
                .name(filmDto.name())
                .description(filmDto.description())
                .releaseDate(filmDto.releaseDate())
                .duration(Duration.ofMinutes(filmDto.duration()))
                .build();
    }
}