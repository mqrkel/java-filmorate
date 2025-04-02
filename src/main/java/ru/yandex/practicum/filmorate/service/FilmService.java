package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;

import java.util.List;

public interface FilmService {
    ResponseFilmDto createFilm(RequestFilmDto filmDto);

    ResponseFilmDto updateFilm(RequestFilmWithIdDto filmDto);

    List<ResponseFilmDto> getAllFilms();

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<ResponseFilmDto> getPopularFilms(Integer count);

    ResponseFilmDto getFilm(Integer filmId);
}