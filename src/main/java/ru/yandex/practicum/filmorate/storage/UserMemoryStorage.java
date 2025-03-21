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

    private boolean checkId(Integer id) {
        return users.containsKey(id);
    }
}