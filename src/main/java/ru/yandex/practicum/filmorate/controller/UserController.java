package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.NewUser;
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
    public User createUser(@RequestBody @Valid NewUser user) {
        validateUser(user);
        User u = new User(getNextId(), user.getEmail(), user.getLogin(), getUserName(user), user.getBirthday());
        users.put(u.getId(), u);
        log.info("Пользователь с id {} создан", u.getId());
        return u;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User newUser) {
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

    public static void validateUser(NewUser user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public static String getUserName(NewUser user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return user.getLogin();
        }
        return user.getName();
    }
}