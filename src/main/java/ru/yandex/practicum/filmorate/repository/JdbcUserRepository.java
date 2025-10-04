package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.NewUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbc;
    private final UserMapper mapper;

    @Override
    public Collection<User> getAllUsers() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, mapper);
    }

    @Override
    public User createUser(NewUser user) {
        String query = "INSERT INTO users (email, login, name, birthday) VALUES(?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getEmail());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            return getUserById(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public User updateUser(User newUser) {
        getUserById(newUser.getId());
        String query = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int rowsUpdated = jdbc.update(
                query,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        if (rowsUpdated == 0) {
            throw new NotFoundException("Пользователь с id " + newUser.getId() + " не найден");
        }
        return newUser;
    }

    @Override
    public User getUserById(long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbc.queryForObject(query, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя с id " + id + " не существует");
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        getUserById(id);
        String query = """
                SELECT u.* FROM users u\s
                JOIN friendships f ON u.id = f.friend_id\s
                WHERE f.user_id = ?""";
        return jdbc.query(query, mapper, id);
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherId) {
        getUserById(userId);
        getUserById(otherId);
        String query = """
                SELECT u.*  \s
                FROM users u \s
                JOIN friendships f1 ON u.id = f1.friend_id \s
                JOIN friendships f2 ON u.id = f2.friend_id \s
                WHERE f1.user_id = ? AND f2.user_id = ?;""";
        return jdbc.query(query, mapper, userId, otherId);
    }
}
