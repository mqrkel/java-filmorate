package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    Optional<User> getUserById(int id);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getUserFriends(Integer userId);

    List<User> getCommonFriends(Integer userId, Integer friendId);

    void deleteUserById(Integer userId);
}