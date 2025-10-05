package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTest {

    private Validator validator;

    @Test
    void isValidateForCorrectFilm() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(2006, 12, 28),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений для валидного фильма");
    }

    @Test
    void isValidateForNullName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                " ",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(2006, 12, 28),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Название фильма не может быть пустым");
    }

    @Test
    void isNotValidateForZeroDuration() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(2006, 12, 28),
                0,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Продолжительность фильма не может быть 0");
    }

    @Test
    void isNotValidateForNegativeDuration() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(2006, 12, 28),
                -1,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Продолжительность фильма не может быть отрицательной");
    }

    @Test
    void isNotValidateForBigDescription201() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своём желании всегда и во всём побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял. С помощью команды друзей он стал чемпионом, но понял, что для негооо не это главное.",
                LocalDate.of(2006, 12, 28),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Длина описания не должна превышать 200 символов");
    }

    @Test
    void isValidateForBigDescription200() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своём желании всегда и во всём побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял. С помощью команды друзей он стал чемпионом, но понял, что для негоо не это главное.",
                LocalDate.of(2006, 12, 28),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений для валидного фильма");
    }

    @Test
    void isNotValidateForReleaseDate2712() {
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(1895, 12, 27),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validateFilm(film), "Исключение не пробросилось");
        Assertions.assertEquals("Дата релиза должна быть указана (начиная с 28 декабря 1895 года)", e.getMessage());
    }

    @Test
    void isValidateForReleaseDate2812() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Film film = new Film(
                1,
                "Тачки",
                "Неукротимый в своем желании всегда и во всем побеждать гоночный автомобиль «Молния» Маккуин сбился с пути и застрял.",
                LocalDate.of(1895, 12, 28),
                112,
                List.of(new FilmGenre(3, "Мультфильм")),
                1
        );
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений для валидного фильма");
    }
}
