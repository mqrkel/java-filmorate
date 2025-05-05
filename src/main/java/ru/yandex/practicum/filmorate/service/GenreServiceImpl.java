package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.GenreMapper;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> getAllGenres() {
        log.info("Запрос списка всех жанров");
        return genreDao.getAllGenres().stream()
                .map(genreMapper::toDto)
                .sorted(Comparator.comparingInt(GenreDto::id))
                .toList();
    }

    @Override
    public GenreDto getGenreById(int id) {
        log.info("Запрос жанра c id={}", id);
        return genreDao.getGenreById(id)
                .map(genreMapper::toDto)
                .orElseThrow(() -> {
                    String msg = "Жанр с id=%d не найден".formatted(id);
                    log.warn(msg);
                    return new NotFoundException(msg);
                });
    }
}