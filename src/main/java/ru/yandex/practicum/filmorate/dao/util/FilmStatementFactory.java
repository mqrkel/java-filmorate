package ru.yandex.practicum.filmorate.dao.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;

@UtilityClass
public final class FilmStatementFactory {

    public static PreparedStatement createFilmInsertStatement(Connection connection, Film film, String sqlQuery) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, film.getName());
        preparedStatement.setString(2, film.getDescription());
        preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
        preparedStatement.setInt(4, film.getDuration());
        preparedStatement.setInt(5, film.getMpa().getId());
        return preparedStatement;
    }
}