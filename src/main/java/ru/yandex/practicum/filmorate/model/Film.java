package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends NewFilm {
    @Min(value = 1, message = "Id должен быть положительным числом")
    private long id;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        super(name, description, releaseDate, duration);
        this.id = id;
    }
}
