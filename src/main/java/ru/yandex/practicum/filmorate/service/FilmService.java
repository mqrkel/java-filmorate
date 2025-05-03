package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;

import java.util.List;

public interface FilmService {

    List<FilmResponseDto> getFilms();

    FilmResponseDto getFilmById(int id);

    FilmResponseDto createFilm(FilmRequestDto dto);

    FilmResponseDto updateFilm(FilmRequestDto dto);

    void deleteFilm(final int id);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<FilmResponseDto> getPopularFilms(Integer count);
}