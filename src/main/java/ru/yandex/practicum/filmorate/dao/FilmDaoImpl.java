package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.util.FilmStatementFactory;
import ru.yandex.practicum.filmorate.exception.DataMappingException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dao.FilmSql.*;

@RequiredArgsConstructor
@Repository
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Film> filmRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FILM_IDS_PARAM = "filmIds";

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = jdbcTemplate.query(GET_ALL_FILMS.getQuery(), filmRowMapper);
        return enrichFilms(films);
    }

    @Override
    public Film createFilm(Film film) {
        validateAttributes(film);
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection ->
                FilmStatementFactory.createFilmInsertStatement(connection, film, INSERT_FILM.getQuery()), keyHolder);

        var filmId = Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue)
                .orElseThrow(() -> new NotFoundException("Не удалось получить ID созданного фильма"));

        film.setId(filmId);
        saveFilmGenres(film);
        return enrichFilms(List.of(film)).get(0);
    }

    @Override
    public Film updateFilm(Film film) {
        validateAttributes(film);

        jdbcTemplate.update(UPDATE_FILM.getQuery(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        saveFilmGenres(film);
        return enrichFilms(List.of(film)).get(0);
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update(DELETE_GENRES.getQuery(), id);
        jdbcTemplate.update(DELETE_FILMS.getQuery(), id);
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        List<Film> films = jdbcTemplate.query(GET_FILM_BY_ID.getQuery(), filmRowMapper, filmId);
        return Optional.ofNullable(films.isEmpty() ? null : enrichFilms(films).get(0));
    }


    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = jdbcTemplate.query(GET_POPULAR_FILM.getQuery(), filmRowMapper, count);
        return enrichFilms(films);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(ADD_LIKE.getQuery(), filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(REMOVE_LIKE.getQuery(), filmId, userId);
    }

    private void saveFilmGenres(Film film) {
        jdbcTemplate.update(DELETE_GENRES.getQuery(), film.getId());

        var genres = new LinkedHashSet<>(Optional.ofNullable(film.getGenres())
                .orElse(Collections.emptySet()));

        if (!genres.isEmpty()) {
            jdbcTemplate.batchUpdate(GENRE_INSERT.getQuery(), genres, genres.size(),
                    (ps, genre) -> {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genre.getId());
                    });
        }
    }

    private void validateFilmGenres(Film film) {
        Set<Integer> genreIds = Optional.ofNullable(film.getGenres())
                .orElse(Collections.emptySet())
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        if (genreIds.isEmpty()) return;

        Set<Integer> existingGenreIds = getExistingGenreIds(genreIds);

        genreIds.forEach(genreId -> {
            if (!existingGenreIds.contains(genreId)) {
                throw new NotFoundException("Жанр с id=" + genreId + " не найден");
            }
        });
    }

    private Set<Integer> getExistingGenreIds(Set<Integer> genreIds) {
        String sql = "SELECT GENRE_ID FROM GENRES WHERE GENRE_ID IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", genreIds);

        return new HashSet<>(namedParameterJdbcTemplate.queryForList(sql, parameters, Integer.class));
    }

    private void validateMpaExists(Mpa mpa) {
        if (mpa == null || !mpaExists(mpa.getId())) {
            throw new NotFoundException("MPA с id=" + (mpa != null ? mpa.getId() : null) + " не найден");
        }
    }

    private boolean mpaExists(int mpaId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                        CHECK_EXIST_MPA_ID.getQuery(), Integer.class, mpaId))
                .map(count -> count > 0)
                .orElse(false);
    }

    private void validateAttributes(Film film) {
        validateMpaExists(film.getMpa());
        validateFilmGenres(film);
    }


    private List<Film> enrichFilms(List<Film> films) {
        if (films.isEmpty()) return films;

        List<Film> enrichedFilms = new ArrayList<>(films);
        Map<Integer, Film> filmMap = enrichedFilms.stream()
                .collect(Collectors.toMap(Film::getId, f -> f));
        List<Integer> filmIds = new ArrayList<>(filmMap.keySet());

        enrichEntities(filmIds, filmMap, SELECT_GENRE_NAME_GENRE_ID_BY_FILM_ID.getQuery(), this::enrichGenres);
        enrichEntities(filmIds, filmMap, SELECT_IDS_USERS_LIKE.getQuery(), this::enrichLikes);
        enrichEntities(filmIds, filmMap, SELECT_MPA_ID_AND_NAME.getQuery(), this::enrichMpa);

        return enrichedFilms;
    }


    private void enrichEntities(List<Integer> filmIds,
                                Map<Integer, Film> filmMap,
                                String query,
                                BiConsumer<ResultSet, Film> enrichmentFunction) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(FILM_IDS_PARAM, filmIds);

        namedParameterJdbcTemplate.query(query, params, rs -> {
            int filmId = rs.getInt("FILM_ID");
            Film film = filmMap.get(filmId);
            enrichmentFunction.accept(rs, film);
        });
    }


    private void enrichGenres(ResultSet rs, Film film) {
        try {
            if (film.getGenres() == null) {
                film.setGenres(new LinkedHashSet<>());
            }
            Genre genre = new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
            film.getGenres().stream()
                    .filter(g -> g.getId().equals(genre.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingGenre -> existingGenre.setName(genre.getName()),
                            () -> film.getGenres().add(genre)
                    );
        } catch (SQLException e) {
            throw new DataMappingException("Ошибка при извлечении жанра из ResultSet для фильма с ID=" + film.getId(), e);
        }
    }

    private void enrichLikes(ResultSet rs, Film film) {
        try {
            if (film.getLikeIds() == null) {
                film.setLikeIds(new HashSet<>());
            }
            int userId = rs.getInt("USER_ID");
            film.getLikeIds().add(userId);
        } catch (SQLException e) {
            throw new DataMappingException("Ошибка при маппинге лайков из ResultSet для фильма с ID=" + film.getId(), e);
        }
    }

    private void enrichMpa(ResultSet rs, Film film) {
        try {
            Mpa mpa = new Mpa(rs.getInt("MPA_RATING_ID"), rs.getString("NAME"));
            film.setMpa(mpa);
        } catch (SQLException e) {
            throw new DataMappingException("Ошибка при маппинге MPA из ResultSet для фильма с ID=" + film.getId(), e);
        }
    }
}