package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class FilmMemoryStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

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

    private boolean checkId(Integer id) {
        return id != null && films.containsKey(id);
    }
}