package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        validateUser(user);
        user.setId(getNextId());
        user.setName(getUserName(user));
        users.put(user.getId(), user);
        log.info("Пользователь с id {} создан", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User newUser) {
        if (newUser.getId() <= 0) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        validateUser(newUser);
        if (users.containsKey(newUser.getId())) {
            newUser.setName(getUserName(newUser));
            users.put(newUser.getId(), newUser);
            log.info("Пользователь с id {} обновлён", newUser.getId());
            return users.get(newUser.getId());
        }
        throw new NotFoundException("Пользователя с id " + newUser.getId() + " не существует");
    }

    private long getNextId() {
        return nextId++;
    }

    public static void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public static String getUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return user.getLogin();
        }
        return user.getName();
    }
}