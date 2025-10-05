package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends NewUser {
    @Min(value = 1, message = "Id должен быть положительным числом")
    private long id;
    private final Map<Long, Friendship> friends = new HashMap<>();
    private final Set<Long> likedFilms = new HashSet<>();

    public User(long id, String email, String login, String name, LocalDate birthday) {
        super(email, login, name, birthday);
        this.id = id;
    }
}
