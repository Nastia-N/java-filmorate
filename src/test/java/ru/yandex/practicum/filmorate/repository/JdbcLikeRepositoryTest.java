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
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.repository.mapper.IdMapper;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcLikeRepository.class, JdbcFilmRepository.class, JdbcUserRepository.class,
FilmMapper.class, JdbcGenreRepository.class, IdMapper.class, UserMapper.class, GenreMapper.class})
public class JdbcLikeRepositoryTest {
    private final JdbcLikeRepository likeRepository;
    private final JdbcFilmRepository filmRepository;
    private final JdbcUserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void isValidateAddLike() {
        Film film = new Film();
        film.setName("Тачки");
        film.setDescription("Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(112);
        film.setGenres(List.of(new FilmGenre(3, "Мультфильм")));
        film.setMpa(new MpaRating(1,"G"));
        Film createdFilm = filmRepository.createFilm(film);

        User user = new User();
        user.setEmail("null@ya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1995, 8, 22));
        User createdUser = userRepository.createUser(user);

        likeRepository.addLike(createdFilm.getId(), createdUser.getId());
        Integer likeCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?",
                Integer.class, createdFilm.getId(), createdUser.getId());
        assertThat(likeCount).isEqualTo(1);
        Integer totalLikes = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM likes", Integer.class);
        assertThat(totalLikes).isEqualTo(1);
    }

    @Test
    void isValidateRemoveLike() {
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
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                "name",
                LocalDate.of(1995, 8, 22)
        );
        userRepository.createUser(user);
        likeRepository.addLike(film.getId(), user.getId());
        Integer likeCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?",
                Integer.class, film.getId(), user.getId());
        assertThat(likeCount).isEqualTo(1);
        likeRepository.removeLike(film.getId(), user.getId());
        Integer finalLikeCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?",
                Integer.class, film.getId(), user.getId());
        assertThat(finalLikeCount).isEqualTo(0);
    }
}
