package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserMemoryStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    private Integer generateId() {
        return idGenerator.getAndIncrement();
    }

    @Override
    public User addUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (!checkId(user.getId())) {
            return Optional.empty();
        }
        users.replace(user.getId(), user);
        return Optional.of(users.get(user.getId()));
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(int id) {
        if (!checkId(id)) {
            return Optional.empty();
        }
        User user = users.get(id);
        return Optional.of(user);
    }

    @Override
    public Optional<Boolean> addFriend(Integer userId, Integer friendId) {
        if (!checkId(userId) || !checkId(friendId)) {
            return Optional.empty();
        }
        User user = users.get(userId);
        User friend = users.get(friendId);

        user.addFriendship(friendId);
        friend.addFriendship(userId);

        return Optional.of(true);
    }

    @Override
    public Optional<Boolean> removeFriend(Integer userId, Integer friendId) {
        if (!checkId(userId) || !checkId(friendId)) {
            return Optional.empty();
        }
        User user = users.get(userId);
        User friend = users.get(friendId);

        user.removeFriendship(friendId);
        friend.removeFriendship(userId);

        return Optional.of(true);
    }

    @Override
    public Optional<List<User>> getUserFriends(Integer userId) {
        if (!checkId(userId)) {
            return Optional.empty();
        }
        User user = users.get(userId);

        List<User> friends = user.getFriendshipIds().stream()
                .map(users::get)
                .toList();

        return Optional.of(friends);
    }

    @Override
    public Optional<List<User>> getCommonFriends(Integer userId, Integer friendId) {
        if (!checkId(userId) || !checkId(friendId)) {
            return Optional.empty();
        }
        User user = users.get(userId);
        User friend = users.get(friendId);

        Set<Integer> commonFriendIds = user.getFriendshipIds();
        commonFriendIds.retainAll(friend.getFriendshipIds());

        List<User> commonFriends = commonFriendIds.stream()
                .map(users::get)
                .toList();

        return Optional.of(commonFriends);
    }

    @Override
    public Optional<User> getUser(Integer id) {
        if (!checkId(id)) {
            return Optional.empty();
        }
        User user = users.get(id);
        return Optional.of(user);
    }

    private boolean checkId(Integer id) {
        return users.containsKey(id);
    }
}