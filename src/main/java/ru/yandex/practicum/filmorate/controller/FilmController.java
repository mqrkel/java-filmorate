package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<ResponseFilmDto> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseFilmDto addFilm(@Valid @RequestBody RequestFilmDto filmDto) {
        return filmService.createFilm(filmDto);
    }

    @PutMapping
    public ResponseFilmDto updateFilm(@Valid @RequestBody RequestFilmWithIdDto filmDto) {
        return filmService.updateFilm(filmDto);
    }

    @GetMapping("/{id}")
    public ResponseFilmDto getFilmById(@PathVariable @Positive Integer id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeFilm(@PathVariable @Positive Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeFilm(@PathVariable @Positive Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<ResponseFilmDto> getPopularFilms(@RequestParam(
            name = "count", defaultValue = "10") @Positive Integer count) {
        return filmService.getPopularFilms(count);
    }
}