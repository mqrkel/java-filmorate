package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ResponseFilmDto createFilm(RequestFilmDto filmDto) {
        log.info("Попытка создать фильм: {}", filmDto);
        try {
            Film film = FilmMapper.convertToFilm(filmDto);
            filmStorage.addFilm(film);
            log.info("Фильм с id={} успешно создан", film.getId());
            return FilmMapper.convertToResponseFilmDto(film);
        } catch (Exception e) {
            log.error("Ошибка при создании фильма: {}. Причина: {}", filmDto, e.getMessage(), e);
            throw new IllegalArgumentException("Не удалось создать фильм: " + filmDto, e);
        }
    }

    @Override
    public ResponseFilmDto updateFilm(RequestFilmWithIdDto filmDto) {
        log.info("Попытка обновить фильм с id={}", filmDto.id());
        try {
            Film film = FilmMapper.convertToFilm(filmDto);
            Film updatedFilm = filmStorage.updateFilm(film)
                    .orElseThrow(() -> {
                        final String msg = "Фильм с id= %d не найден".formatted(filmDto.id());
                        log.warn(msg);
                        return new NotFoundException(msg);
                    });
            log.info("Фильм с id={} успешно обновлён: {}", filmDto.id(), updatedFilm);
            return FilmMapper.convertToResponseFilmDto(updatedFilm);
        } catch (Exception e) {
            log.error("Ошибка при обновлении фильма с id={}. Причина: {}", filmDto.id(), e.getMessage(), e);
            throw new UpdateException("Не удалось обновить фильм с id=" + filmDto.id(), e);
        }
    }

    @Override
    public List<ResponseFilmDto> getAllFilms() {
        log.info("Запрос списка всех фильмов.");
        List<Film> films = filmStorage.getFilms();
        return films.stream()
                .map(FilmMapper::convertToResponseFilmDto)
                .toList();
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        log.info("Добавление лайка фильму с id={} от пользователя id={}", filmId, userId);
        if (userStorage.getUserById(userId).isEmpty()) {
            log.warn("Не найден пользователь с id={}", userId);
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        boolean result = filmStorage.addLike(filmId, userId)
                .orElseThrow(() -> {
                    final String msg = "Фильм с id=%d не найден".formatted(filmId);
                    log.warn(msg);
                    return new NotFoundException("Фильм с id=%d не найден".formatted(filmId));
                });
        if (result) {
            log.info("Лайк успешно добавлен.");
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        log.info("Попытка удалить лайк у фильма с id={} от пользователя с id={}", filmId, userId);
        if (userStorage.getUserById(userId).isEmpty()) {
            log.warn("Не найден пользователь с id={}", userId);
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        boolean result = filmStorage.removeLike(filmId, userId)
                .orElseThrow(() -> {
                    final String msg = "Фильм с id=%d не найден".formatted(filmId);
                    log.warn(msg);
                    return new NotFoundException("Фильм с id=%d не найден".formatted(filmId));
                });
        if (result) {
            log.info("Лайк успешно удалён.");
        }
    }

    @Override
    public List<ResponseFilmDto> getPopularFilms(Integer count) {
        log.info("Запрос популярных фильмов, количество: {}", count);
        return filmStorage.getPopularFilms(count).stream()
                .map(FilmMapper::convertToResponseFilmDto)
                .toList();
    }

    @Override
    public ResponseFilmDto getFilm(Integer id) {
        log.info("Получение фильма с id={}", id);
        Film film = filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=%d не найден".formatted(id)));
        return FilmMapper.convertToResponseFilmDto(film);
    }
}