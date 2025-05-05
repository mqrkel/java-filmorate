package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    Optional<Film> getFilmById(Integer filmId);

    List<Film> getPopularFilms(Integer count);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}