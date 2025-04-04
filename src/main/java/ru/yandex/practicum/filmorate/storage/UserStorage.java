package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    Optional<User> updateUser(User user);

    List<User> getUsers();

    Optional<User> getUserById(int id);

    Optional<Boolean> addFriend(Integer userId, Integer friendId);

    Optional<Boolean> removeFriend(Integer userId, Integer friendId);

    Optional<List<User>> getUserFriends(Integer userId);

    Optional<List<User>> getCommonFriends(Integer userId, Integer friendId);

    Optional<User> getUser(Integer id);
}