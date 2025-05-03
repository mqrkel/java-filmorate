package ru.yandex.practicum.filmorate.dao.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;

@UtilityClass
public final class UserStatementFactory {

    public static PreparedStatement createUserInsertStatement(Connection connection, User user, String sqlQuery) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getLogin());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
        return preparedStatement;
    }
}