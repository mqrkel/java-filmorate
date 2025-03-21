package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.exception.FilmCreationException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmUpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public ResponseFilmDto createFilm(RequestFilmDto filmDto) {
        log.info("Попытка создать фильм: {}", filmDto);
        try {
            Film film = converToFilm(filmDto);
            filmStorage.addFilm(film);
            log.info("Фильм с id={} успешно создан", film.getId());
            return convertToResponseFilmDto(film);
        } catch (Exception e) {
            log.error("Ошибка при создании фильма: {}. Причина: {}", filmDto, e.getMessage(), e);
            throw new FilmCreationException("Не удалось создать фильм: " + filmDto, e);
        }
    }

    @Override
    public ResponseFilmDto updateFilm(RequestFilmWithIdDto filmDto) {
        log.info("Попытка обновить фильм с id={}", filmDto.id());
        try {
            Film film = convertToFilm(filmDto);
            Film updatedFilm = filmStorage.updateFilm(film).orElseThrow(() -> {
                final String msg = "Фильм с id= %d не найден".formatted(filmDto.id());
                log.warn(msg);
                return new FilmNotFoundException(msg);
            });
            log.info("Фильм с id={} успешно обновлён: {}", filmDto.id(), updatedFilm);
            return convertToResponseFilmDto(updatedFilm);
        } catch (Exception e) {
            log.error("Ошибка при обновлении фильма с id={}. Причина: {}", filmDto.id(), e.getMessage(), e);
            throw new FilmUpdateException("Не удалось обновить фильм с id=" + filmDto.id(), e);
        }
    }

    @Override
    public List<ResponseFilmDto> getAllFilms() {
        log.info("Запрос списка всех фильмов.");
        List<Film> films = filmStorage.getFilms();
        return films.stream()
                .map(this::convertToResponseFilmDto)
                .toList();
    }

    private ResponseFilmDto convertToResponseFilmDto(Film updatedFilm) {
        return ResponseFilmDto.builder()
                .id(updatedFilm.getId())
                .name(updatedFilm.getName())
                .description(updatedFilm.getDescription())
                .releaseDate(updatedFilm.getReleaseDate())
                .duration((int) updatedFilm.getDuration().toMinutes())
                .build();
    }

    private Film converToFilm(RequestFilmDto filmDto) {
        return Film.builder()
                .name(filmDto.name())
                .description(filmDto.description())
                .releaseDate(filmDto.releaseDate())
                .duration(Duration.ofMinutes(filmDto.duration()))
                .build();
    }

    private Film convertToFilm(RequestFilmWithIdDto filmDto) {
        return Film.builder()
                .id(filmDto.id())
                .name(filmDto.name())
                .description(filmDto.description())
                .releaseDate(filmDto.releaseDate())
                .duration(Duration.ofMinutes(filmDto.duration()))
                .build();
    }
}