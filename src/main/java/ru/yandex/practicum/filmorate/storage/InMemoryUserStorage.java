package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.NewUser;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User createUser(NewUser user) {
        validateUser(user);
        User u = new User(getNextId(), user.getEmail(), user.getLogin(), getUserName(user), user.getBirthday());
        users.put(u.getId(), u);
        log.info("Пользователь с id {} создан", u.getId());
        return u;
    }

    public User updateUser(User newUser) {
        validateUser(newUser);
        if (users.containsKey(newUser.getId())) {
            newUser.setName(getUserName(newUser));
            users.put(newUser.getId(), newUser);
            log.info("Пользователь с id {} обновлён", newUser.getId());
            return users.get(newUser.getId());
        }
        throw new NotFoundException("Пользователя с id " + newUser.getId() + " не существует");
    }

    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователя с id " + id + " не существует");
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
