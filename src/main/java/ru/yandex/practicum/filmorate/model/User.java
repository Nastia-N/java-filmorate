package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends NewUser {
    @Min(value = 1, message = "Id должен быть положительным числом")
    private long id;
    private Set<Long> friends = new HashSet<>();
    private Set<Long> likedFilms = new HashSet<>();

    public User(long id, String email, String login, String name, LocalDate birthday) {
        super(email, login, name, birthday);
        this.id = id;
    }

    public void addFriend(Long id) {
        if (id != null && id > 0) {
            if (this.id == id) {
                throw new ValidationException("Пользователь не может добавить себя в друзья");
            }
            friends.add(id);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }

    public void removeFriend(Long id) {
        if (id != null && id > 0) {
            friends.remove(id);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }

    public void addLikeToFilm(Long filmId) {
        if (filmId != null && filmId > 0) {
            likedFilms.add(filmId);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }

    public void removeLikeToFilm(Long filmId) {
        if (filmId != null && filmId > 0) {
            likedFilms.remove(filmId);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }
}
