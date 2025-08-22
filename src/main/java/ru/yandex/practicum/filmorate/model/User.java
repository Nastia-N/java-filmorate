package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends NewUser {
    @Min(value = 1, message = "Id должен быть положительным числом")
    private long id;

    public User(long id, String email, String login, String name, LocalDate birthday) {
        super(email, login, name, birthday);
        this.id = id;
    }
}
