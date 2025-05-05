package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.util.UserStatementFactory;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dao.UserSql.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection ->
                UserStatementFactory.createUserInsertStatement(connection, user, CREATE_USER.getQuery()), keyHolder);
        var userId = Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue)
                .orElseThrow(() -> new IllegalStateException("Не удалось получить ID созданного пользователя"));
        user.setId(userId);

        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(
                UPDATE_USER.getQuery(),
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(GET_ALL_USERS.getQuery(), userRowMapper);
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return jdbcTemplate.query(GET_USER_BY_ID.getQuery(), userRowMapper, userId)
                .stream()
                .findFirst();
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        if (isFriendshipExists(userId, friendId)) {
            log.warn("Пользователи с id={} и id={} уже являются друзьями.", userId, friendId);
            return;
        }
        jdbcTemplate.update(ADD_FRIEND.getQuery(), userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        if (!isFriendshipExists(userId, friendId)) {
            log.warn("Пользователи с id={} и id={} не являются друзьями.", userId, friendId);
        }
        jdbcTemplate.update(REMOVE_FRIEND.getQuery(), userId, friendId);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        return jdbcTemplate.query(GET_USER_FRIENDS.getQuery(), userRowMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS.getQuery(), userRowMapper, userId, otherId);
    }

    @Override
    public void deleteUserById(Integer userId) {
        jdbcTemplate.update(REMOVE_ALL_FRIENDS.getQuery(), userId);
        jdbcTemplate.update(DELETE_USER.getQuery(), userId);
    }

    private boolean isFriendshipExists(int userId, int friendId) {
        Integer count = jdbcTemplate.queryForObject(CHECK_EXISTING_FRIENDSHIP.getQuery(), Integer.class, userId, friendId);
        return count != null && count > 0;
    }
}