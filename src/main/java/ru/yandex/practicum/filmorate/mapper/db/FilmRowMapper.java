package ru.yandex.practicum.filmorate.mapper.db;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public final class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getObject("FILM_ID", Integer.class))
                .name(rs.getObject("TITLE", String.class))
                .description(rs.getObject("DESCRIPTION", String.class))
                .releaseDate(rs.getObject("RELEASE_DATE", LocalDate.class))
                .duration(rs.getObject("DURATION", Integer.class))
                .mpa(new Mpa(rs.getObject("MPA_RATING_ID", Integer.class), null))
                .build();
    }
}