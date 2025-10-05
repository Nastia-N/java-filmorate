package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreMapper;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final JdbcTemplate jdbc;
    private final GenreMapper genreMapper;

    @Override
    public List<FilmGenre> getAllGenres() {
        String query = "SELECT * FROM genres ORDER BY id";
        return jdbc.query(query, genreMapper);
    }

    @Override
    public FilmGenre getGenreById(int id) {
        String query = "SELECT * FROM genres WHERE id = ?";
        try {
            return jdbc.queryForObject(query, genreMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанра с id " + id + " не существует");
        }
    }

    @Override
    public void saveGenres(Set<Integer> genreIds, long filmId) {
        String delete = "DELETE FROM film_genres WHERE film_id = ?";
        jdbc.update(delete, filmId);
        String query = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Integer g : genreIds) {
            jdbc.update(query, filmId, g);
        }
    }

    @Override
    public List<FilmGenre> getGenresByFilmId(long filmId) {
        String query = "SELECT g.* " +
                "FROM genres g " +
                "JOIN film_genres fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ? " +
                "ORDER BY g.id";
        return jdbc.query(query, genreMapper, filmId);
    }
}
