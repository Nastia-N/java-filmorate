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
public class Film extends NewFilm {
    @Min(value = 1, message = "Id должен быть положительным числом")
    private long id;
    private Set<Long> likes = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Set<Genre> genre, MPA rating) {
        super(name, description, releaseDate, duration, genre, rating);
        this.id = id;
    }

    public void addLike(Long userId) {
        if (userId != null && userId > 0) {
            likes.add(userId);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }

    public void removeLike(Long userId) {
        if (userId != null && userId > 0) {
            likes.remove(userId);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }
}
