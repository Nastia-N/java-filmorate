package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.NewFilm;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final JdbcTemplate jdbc;
    private final FilmMapper mapper;
    private final GenreRepository genreRepository;

    @Override
    public Collection<Film> getAllFilms() {
        String query = """
                SELECT f.*, mr.name AS name_rating
                FROM films f
                INNER JOIN mpa_ratings mr ON f.rating = mr.id""";
        return jdbc.query(query, mapper);
    }

    @Override
    public Film createFilm(NewFilm film) {
        String query = "INSERT INTO films (name, description, release_date, duration, rating) VALUES(?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            Set<Integer> g = film.getGenres().stream().map(FilmGenre::getId)
                    .collect(Collectors.toSet());
            genreRepository.saveGenres(g, id);
            return getFilmWithGenres(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public Film updateFilm(Film newFilm) {
        String query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating = ? WHERE id = ?";
        int rowsUpdated = jdbc.update(
                query,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        if (rowsUpdated == 0) {
            throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
        }
        Set<Integer> g = newFilm.getGenres().stream().map(FilmGenre::getId)
                .collect(Collectors.toSet());
        genreRepository.saveGenres(g, newFilm.getId());
        return newFilm;
    }

    @Override
    public Film getFilmById(long id) {
        String query = """
                SELECT f.*, mr.name AS name_rating
                FROM films f
                INNER JOIN mpa_ratings mr ON f.rating = mr.id
                WHERE f.id = ?""";
        try {
            return jdbc.queryForObject(query, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильма с id " + id + " не существует");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String query = """
                SELECT f.*, mr.name AS name_rating, COUNT(l.user_id) AS likes_count
                FROM films f
                INNER JOIN mpa_ratings mr ON f.rating = mr.id
                LEFT JOIN likes l ON f.id = l.film_id
                GROUP BY f.id
                ORDER BY likes_count DESC, f.name
                LIMIT ?""";
        return jdbc.query(query, mapper, count);
    }

    public Film getFilmWithGenres(long id) {
        Film film = getFilmById(id);
        List<FilmGenre> genres = genreRepository.getGenresByFilmId(id);
        film.setGenres(new ArrayList<>(genres));
        return film;
    }
}
