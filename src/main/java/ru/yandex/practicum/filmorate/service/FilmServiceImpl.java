package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final FilmMapper filmMapper;

    @Override
    public List<FilmResponseDto> getFilms() {
        log.info("Запрос списка всех фильмов.");
        List<Film> films = filmDao.getAllFilms();
        return films.stream().map(filmMapper::toDto).toList();
    }

    @Override
    public FilmResponseDto getFilmById(int filmId) {
        log.info("Получение фильма по id={}", filmId);
        return filmMapper.toDto(getExistingFilm(filmId));
    }


    @Override
    public FilmResponseDto createFilm(FilmRequestDto dto) {
        Film film = filmMapper.toEntity(dto);
        log.info("Создание фильма: {}", film);
        Film createdFilm = filmDao.createFilm(film);
        log.info("Фильм создан с id={}", createdFilm.getId());
        return filmMapper.toDto(createdFilm);
    }

    @Override
    public FilmResponseDto updateFilm(FilmRequestDto dto) {
        Film film = filmMapper.toEntity(dto);
        int id = film.getId();
        log.info("Обновление фильма с id={}", id);
        getExistingFilm(id);
        Film updatedFilm = filmDao.updateFilm(film);
        log.info("Фильм с id={} обновлён", updatedFilm.getId());
        return filmMapper.toDto(updatedFilm);
    }

    @Override
    public void deleteFilm(int id) {
        log.info("Удаление фильма с id={}", id);
        getExistingFilm(id);
        filmDao.deleteFilm(id);
        log.info("Фильм с id={} удалён", id);
    }


    @Override
    public void addLike(Integer filmId, Integer userId) {
        log.info("Добавление лайка: фильмId={}, пользовательId={}", filmId, userId);

        ensureFilmAndUserExist(filmId, userId);

        filmDao.addLike(filmId, userId);
        log.info("Лайк добавлен фильму с id={}", filmId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        log.info("Удаление лайка: фильмId={}, пользовательId={}", filmId, userId);

        ensureFilmAndUserExist(filmId, userId);
        validateUserLikedFilm(filmId, userId);

        filmDao.removeLike(filmId, userId);
        log.info("Лайк удалён у фильма с id={}", filmId);
    }

    @Override
    public List<FilmResponseDto> getPopularFilms(Integer count) {
        log.info("Запрос популярных фильмов, количество: {}", count);
        return filmDao.getPopularFilms(count).stream().map(filmMapper::toDto).toList();
    }

    private void getExistingUser(Integer userId) {
        userDao.getUserById(userId).orElseThrow(() -> {
            String message = "Пользователь с id=" + userId + " не найден";
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private Film getExistingFilm(Integer filmId) {
        return filmDao.getFilmById(filmId).orElseThrow(() -> {
            String message = "Фильм с id=" + filmId + " не найден";
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private void validateUserLikedFilm(Integer filmId, Integer userId) {
        Film film = getExistingFilm(filmId);
        if (!film.getLikeIds().contains(userId)) {
            String msg = String.format("Пользователь с id=%d не ставил лайк фильму с id=%d", userId, filmId);
            log.warn(msg);
            throw new ValidationException(msg);
        }
    }

    private void ensureFilmAndUserExist(Integer filmId, Integer userId) {
        getExistingFilm(filmId);
        getExistingUser(userId);
    }
}