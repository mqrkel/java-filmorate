package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final RowMapper<Mpa> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_MPA_SQL = "SELECT MPA_RATING_ID, NAME FROM MPA_RATINGS";
    private static final String SELECT_MPA_BY_ID_SQL =
            "SELECT MPA_RATING_ID, NAME FROM MPA_RATINGS WHERE MPA_RATING_ID = ?";

    @Override
    public List<Mpa> getAllMpa() {
        return new ArrayList<>(jdbcTemplate.query(SELECT_ALL_MPA_SQL, rowMapper));
    }

    @Override
    public Optional<Mpa> getMpaById(Integer id) {
        return jdbcTemplate.query(SELECT_MPA_BY_ID_SQL, rowMapper, id)
                .stream()
                .findFirst();
    }
}