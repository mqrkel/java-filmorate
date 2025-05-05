package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Create;
import ru.yandex.practicum.filmorate.validator.Update;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmResponseDto> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmResponseDto addFilm(@Validated(Create.class) @Valid @RequestBody FilmRequestDto dto) {
        return filmService.createFilm(dto);
    }

    @PutMapping
    public FilmResponseDto updateFilm(@Validated(Update.class) @Valid @RequestBody FilmRequestDto dto) {
        return filmService.updateFilm(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/{id}")
    public FilmResponseDto getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmResponseDto> getPopularFilms(
            @RequestParam(defaultValue = "10") @Positive Integer count) {
        return filmService.getPopularFilms(count);
    }
}