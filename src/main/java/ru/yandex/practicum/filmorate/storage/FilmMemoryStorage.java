package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class FilmMemoryStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final UserMemoryStorage userMemoryStorage;

    @Autowired
    public FilmMemoryStorage(UserMemoryStorage userMemoryStorage) {
        this.userMemoryStorage = userMemoryStorage;
    }

    private Integer generateId() {
        return idGenerator.getAndIncrement();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (!checkId(film.getId())) {
            return Optional.empty();
        }
        films.replace(film.getId(), film);
        return Optional.of(films.get(film.getId()));
    }

    @Override
    public Optional<Boolean> addLike(Integer filmId, Integer userId) {
        if (!checkId(filmId) || !checkUserExist(userId)) {
            return Optional.empty();
        }
        Film film = films.get(filmId);
        film.addLike(userId);
        return Optional.of(true);
    }

    @Override
    public Optional<Boolean> removeLike(Integer filmId, Integer userId) {
        if (!checkId(filmId) || !checkUserExist(userId)) {
            return Optional.empty();
        }
        Film film = films.get(filmId);
        film.removeLike(userId);
        return Optional.of(true);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes(), f1.getLikes()))
                .limit(count)
                .toList();
    }

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        if (!checkId(filmId)) {
            return Optional.empty();
        }
        return Optional.of(films.get(filmId));
    }

    private boolean checkId(Integer id) {
        return id != null && films.containsKey(id);
    }

    private boolean checkUserExist(Integer userId) {
        return userMemoryStorage.getUserById(userId).isPresent();
    }
}