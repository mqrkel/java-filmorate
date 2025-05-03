package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper;

    private static final String SELECT_ALL_GENRES_SQL = "SELECT GENRE_ID, NAME FROM GENRES";
    private static final String SELECT_GENRE_BY_ID_SQL = "SELECT GENRE_ID, NAME FROM GENRES WHERE GENRE_ID = ?";


    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(SELECT_ALL_GENRES_SQL, genreRowMapper);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return jdbcTemplate.query(SELECT_GENRE_BY_ID_SQL, genreRowMapper, id)
                .stream()
                .findFirst();
    }
}