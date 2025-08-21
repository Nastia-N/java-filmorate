package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {

    @Test
    public void isValidateForNullLogin() {
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
    public void isValidateForFutureBirthday() {
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                null,
                LocalDate.of(2026, 1,1)
        );
        ValidationException e = Assertions.assertThrows(ValidationException.class, () -> UserController.validateUser(user), "Исключение не пробросилось");
        Assertions.assertEquals("Дата рождения не может быть в будущем", e.getMessage());
    }
}
