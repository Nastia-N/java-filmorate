package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends NewFilm {
    @Min(value = 1, message = "Id должен быть положительным числом")
    private long id;
    private Set<Long> likes = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, List<FilmGenre> genre, int rating) {
        super(name, description, releaseDate, duration, genre, new MpaRating(rating, null));
        this.id = id;
    }

    public void addLike(long userId) {
        if (userId > 0) {
            likes.add(userId);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }

    public void removeLike(long userId) {
        if (userId > 0) {
            likes.remove(userId);
        } else {
            throw new ValidationException("Id должен быть положительным числом");
        }
    }
}
