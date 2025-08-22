package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTest {

    private Validator validator;

    @Test
    void isValidateForForCorrectUser() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                "name",
                LocalDate.of(1995, 8,22)
        );
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Не должно быть нарушений для валидного пользователя");
    }

    @Test
    void isNotValidateForEmail() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        User user = new User(
                1,
                "nullya.ru",
                "login",
                null,
                LocalDate.of(1995, 9,1)
        );
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Электронная почта должна содержать символ @");
    }

    @Test
    void isNotValidateForBlankEmail() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        User user = new User(
                1,
                " ",
                "login",
                "name",
                LocalDate.of(1995, 9,1)
        );
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Электронная почта может быть пустой и должна содержать символ @");
    }

    @Test
    public void isNotValidateForLogin() {
        User user = new User(
                1,
                "null@ya.ru",
                "lo gin",
                null,
                LocalDate.of(2006, 1,1)
        );
        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> UserController.validateUser(user), "Исключение не пробросилось");
        Assertions.assertEquals("Логин не может содержать пробелы", e.getMessage());
    }

    @Test
    public void isNotValidateForBirthday2408() {
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                null,
                LocalDate.of(2025, 8,24)
        );
        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> UserController.validateUser(user), "Исключение не пробросилось");
        Assertions.assertEquals("Дата рождения не может быть в будущем", e.getMessage());
    }

    @Test
    void isValidateForBirthday2208() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                null,
                LocalDate.of(2025, 8,22)
        );
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Дата рождения не может быть в будущем");
    }
}
