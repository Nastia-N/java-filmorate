package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.mapper.GenreMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcGenreRepository.class, GenreMapper.class,
        JdbcFilmRepository.class, FilmMapper.class})
public class JdbcGenreRepositoryTest {
    private final JdbcGenreRepository genreRepository;
    private final JdbcFilmRepository filmRepository;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void isValidateGetAllGenres() {
        Collection<FilmGenre> allGenres = genreRepository.getAllGenres();
        assertThat(allGenres).hasSize(6);
    }

    @Test
    void isValidateGetGenreById() {
        assertEquals("Мультфильм", genreRepository.getGenreById(3).getName(), "Жанр не совпадает.");
    }

    @Test
    void isValidateGetGenresByFilmId() {
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(1895, 12, 27),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        filmRepository.createFilm(film);
        genreRepository.getGenresByFilmId(film.getId());
        assertEquals("Мультфильм", genreRepository.getGenreById(3).getName(), "Жанр не совпадает.");
    }

    @Test
    void isValidateSaveGenres() {
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(1895, 12, 27),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );

        Film createdFilm = filmRepository.createFilm(film);
        long filmId = createdFilm.getId();

        Set<Integer> genreIds = Set.of(1, 2);
        genreRepository.saveGenres(genreIds, filmId);

        List<Integer> savedGenres = jdbcTemplate.queryForList(
                "SELECT genre_id FROM film_genres WHERE film_id = ? ORDER BY genre_id",
                Integer.class, filmId);
        assertThat(savedGenres).hasSize(2);
        assertThat(savedGenres).containsExactly(1, 2);
    }
}
