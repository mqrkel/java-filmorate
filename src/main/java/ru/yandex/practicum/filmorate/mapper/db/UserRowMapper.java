package ru.yandex.practicum.filmorate.mapper.db;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getObject("user_id", Integer.class))
                .email(rs.getObject("email", String.class))
                .name(rs.getObject("full_name", String.class))
                .login(rs.getObject("login", String.class))
                .birthday(rs.getObject("birth_date", LocalDate.class))
                .build();
    }
}