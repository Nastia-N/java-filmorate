package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.mapper.MPAMapper;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class JdbcMPARepository implements MPARepository {
    private final JdbcTemplate jdbc;
    private final MPAMapper mpaMapper;

    @Override
    public Collection<MpaRating> getAllRating() {
        String query = "SELECT * FROM mpa_ratings ORDER BY id";
        return jdbc.query(query, mpaMapper);
    }

    @Override
    public MpaRating getRatingById(int id) {
        String query = "SELECT * FROM mpa_ratings WHERE id = ?";
        try {
            return jdbc.queryForObject(query, mpaMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинга с id " + id + " не существует");
        }
    }
}
