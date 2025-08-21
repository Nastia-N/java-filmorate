package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTest {

    @Test
    public void isValidateForLastReleaseDate() {
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(1895, 1,1),
                112
        );
        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> FilmController.validateFilm(film), "Исключение не пробросилось");
        Assertions.assertEquals("Дата релиза должна быть указана (начиная с 28 декабря 1895 года)", e.getMessage());
    }
}
