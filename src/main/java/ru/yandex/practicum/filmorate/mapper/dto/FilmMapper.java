package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper {

    private final GenreMapper genreMapper;
    private final MpaMapper mpaMapper;

    public FilmResponseDto toDto(Film film) {
        return FilmResponseDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .likeIds(film.getLikeIds() != null ? film.getLikeIds() : new HashSet<>())
                .mpa(film.getMpa() != null ? mpaMapper.toDto(film.getMpa()) : null)
                .genres(fillGenreDto(film))
                .build();
    }

    public Film toEntity(FilmRequestDto dto) {
        return Film.builder()
                .id(dto.id() != null ? dto.id() : null)
                .name(dto.name())
                .description(dto.description())
                .releaseDate(dto.releaseDate())
                .duration(dto.duration())
                .mpa(dto.mpa() != null ? dto.mpa() : null)
                .genres(dto.genres() != null ? dto.genres() : new LinkedHashSet<>())
                .build();
    }

    private Set<GenreDto> fillGenreDto(Film film) {
        return film.getGenres() != null
                ? film.getGenres().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new))
                : new LinkedHashSet<>();
    }
}