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
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userRepository.createUser(user);

        User user1 = new User();
        user1.setEmail("user1@mail.ru");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1992, 1, 1));
        User createdUser1 = userRepository.createUser(user1);

        friendshipRepository.addFriend(createdUser.getId(), createdUser1.getId());

        Integer friendshipCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?",
                Integer.class, createdUser.getId(), createdUser1.getId());

        assertThat(friendshipCount).isEqualTo(1);

        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM friendships WHERE user_id = ? AND friend_id = ?",
                String.class, createdUser.getId(), createdUser1.getId());
        assertThat(status).isEqualTo("SENDING");
    }

    @Test
    void isValidateRemoveFriend() {
        // Создаем первого пользователя
        User user = new User();
        user.setEmail("null@ya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1995, 8, 22));
        User createdUser = userRepository.createUser(user);

        // Создаем второго пользователя с другим email и login
        User user1 = new User();
        user1.setEmail("null1@ya.ru");
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setBirthday(LocalDate.of(1992, 8, 22));
        User createdUser1 = userRepository.createUser(user1);

        friendshipRepository.addFriend(createdUser.getId(), createdUser1.getId());
        Integer friendshipCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?",
                Integer.class, createdUser.getId(), createdUser1.getId());
        assertThat(friendshipCount).isEqualTo(1);
        friendshipRepository.removeFriend(createdUser.getId(), createdUser1.getId());
        Integer friendshipCountNew = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?",
                Integer.class, createdUser.getId(), createdUser1.getId());
        assertThat(friendshipCountNew).isEqualTo(0);
    }
}
