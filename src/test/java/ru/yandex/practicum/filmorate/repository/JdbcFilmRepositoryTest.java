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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.repository.mapper.IdMapper;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcFilmRepository.class, GenreMapper.class, JdbcUserRepository.class, UserMapper.class,
        FilmMapper.class, JdbcGenreRepository.class, IdMapper.class})
class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository filmRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserRepository userRepository;

    @Test
    void isValidateCreatedFilm() {
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
        assertEquals(112, film.getDuration(), "Продолжительность фильма не совпадает.");
        assertEquals("Тачки", film.getName(), "Название фильма не совпадает.");
        assertEquals("Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.", film.getDescription(), "Описание фильма не совпадает.");
        assertEquals(LocalDate.of(1895, 12, 27), film.getReleaseDate(), "Дата выхода фильма не совпадает.");
        assertEquals(List.of(new FilmGenre(3, "Мультфильм")), film.getGenres(), "Жанр фильма не совпадает.");
    }

    @Test
    void isValidateUpdateFilm() {
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
        Film updateFilm = new Film(
                2,
                "Тачки2",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(1895, 12, 27),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        filmRepository.updateFilm(updateFilm);
        assertEquals(112, updateFilm.getDuration(), "Продолжительность фильма не совпадает.");
        assertEquals("Тачки2", updateFilm.getName(), "Название фильма не обновилось.");
    }

    @Test
    void isValidateGetFilmById() {
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
        Film film1 = filmRepository.getFilmById(1);
        assertEquals(112, film.getDuration(), "Продолжительность фильма не совпадает.");
        assertEquals("Тачки", film.getName(), "Название фильма не совпадает.");
        assertEquals("Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.", film.getDescription(), "Описание фильма не совпадает.");
        assertEquals(LocalDate.of(1895, 12, 27), film.getReleaseDate(), "Дата выхода фильма не совпадает.");
        assertEquals(List.of(new FilmGenre(3, "Мультфильм")), film.getGenres(), "Жанр фильма не совпадает.");
    }

    @Test
    void isValidateGetAllFilms() {
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
        Film film1 = new Film(
                2,
                "Тачки2",
                "Автомобиль «Молния» Маккуин.",
                LocalDate.of(1897, 12, 27),
                110,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        filmRepository.createFilm(film1);
        Collection<Film> allFilms = filmRepository.getAllFilms();
        assertThat(allFilms).hasSize(2);
        assertThat(allFilms)
                .extracting(Film::getName)
                .containsExactlyInAnyOrder("Тачки", "Тачки2");
    }

    @Test
    void isValidateGetPopularFilms() {
        Film film1 = new Film(
                1,
                "Фильм с лайками",
                "Описание 1",
                LocalDate.of(2020, 1, 1),
                120,
                new ArrayList<>(),
                1
        );
        Film createdFilm1 = filmRepository.createFilm(film1);

        Film film2 = new Film(
                2,
                "Фильм без лайков",
                "Описание 2",
                LocalDate.of(2021, 1, 1),
                90,
                new ArrayList<>(),
                2
        );
        Film createdFilm2 = filmRepository.createFilm(film2);

        Film film3 = new Film(
                3,
                "Фильм с большим количеством лайков",
                "Описание 3",
                LocalDate.of(2022, 1, 1),
                150,
                new ArrayList<>(),
                1
        );
        Film createdFilm3 = filmRepository.createFilm(film3);

        User user = new User(
                1,
                "null@ya.ru",
                "login",
                "name",
                LocalDate.of(1995, 8, 22)
        );
        userRepository.createUser(user);

        User user1 = new User(
                2,
                "null1@ya.ru",
                "login1",
                "name1",
                LocalDate.of(1993, 8, 22)
        );
        userRepository.createUser(user1);

        User user2 = new User(
                1,
                "null2@ya.ru",
                "login2",
                "name2",
                LocalDate.of(1993, 8, 22)
        );
        userRepository.createUser(user2);

        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", createdFilm3.getId(), 1);
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", createdFilm3.getId(), 2);
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", createdFilm3.getId(), 3);

        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", createdFilm1.getId(), 1);
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", createdFilm1.getId(), 2);

        List<Film> popularFilms = filmRepository.getPopularFilms(2);
        assertThat(popularFilms).hasSize(2);
        assertThat(popularFilms.get(0).getId()).isEqualTo(createdFilm3.getId());
        assertThat(popularFilms.get(0).getName()).isEqualTo("Фильм с большим количеством лайков");

        assertThat(popularFilms.get(1).getId()).isEqualTo(createdFilm1.getId());
        assertThat(popularFilms.get(1).getName()).isEqualTo("Фильм с лайками");
        assertThat(popularFilms)
                .extracting(Film::getId)
                .doesNotContain(createdFilm2.getId());

        List<Film> topFilm = filmRepository.getPopularFilms(1);
        assertThat(topFilm).hasSize(1);
        assertThat(topFilm.getFirst().getId()).isEqualTo(createdFilm3.getId());
    }
}