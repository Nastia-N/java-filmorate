package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcFriendshipRepository.class, JdbcUserRepository.class, UserMapper.class})
public class JdbcFriendshipRepositoryTest {
    private final JdbcFriendshipRepository friendshipRepository;
    private final JdbcUserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void isValidateAddFriend() {
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                "name",
                LocalDate.of(1995, 8, 22)
        );
        userRepository.createUser(user);

        User user1 = new User(
                2,
                "null1@ya.ru",
                "login1",
                "name1",
                LocalDate.of(1992, 8, 22)
        );
        userRepository.createUser(user1);
        friendshipRepository.addFriend(user.getId(), user1.getId());

        Integer friendshipCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?",
                Integer.class, user.getId(), user1.getId());

        assertThat(friendshipCount).isEqualTo(1);
        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM friendships WHERE user_id = ? AND friend_id = ?",
                String.class, user.getId(), user1.getId());
        assertThat(status).isEqualTo("SENDING");
    }

    @Test
    void isValidateRemoveFriend() {
        User user = new User(
                1,
                "null@ya.ru",
                "login",
                "name",
                LocalDate.of(1995, 8, 22)
        );
        userRepository.createUser(user);

        User user1 = new User(
                1,
                "null1@ya.ru",
                "login1",
                "name1",
                LocalDate.of(1992, 8, 22)
        );
        userRepository.createUser(user1);

        friendshipRepository.addFriend(user.getId(), user1.getId());
        Integer friendshipCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?",
                Integer.class, user.getId(), user1.getId());
        assertThat(friendshipCount).isEqualTo(1);
        friendshipRepository.removeFriend(user.getId(), user1.getId());
        Integer friendshipCountNew = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?",
                Integer.class, user.getId(), user1.getId());
        assertThat(friendshipCountNew).isEqualTo(0);
    }
}
