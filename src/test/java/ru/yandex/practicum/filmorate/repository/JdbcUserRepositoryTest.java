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
import java.util.Collection;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcUserRepository.class, UserMapper.class, JdbcFriendshipRepository.class})
public class JdbcUserRepositoryTest {
    private final JdbcUserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcFriendshipRepository friendshipRepository;

    @Test
    void isValidateUpdateUser() {
        User user = new User();
        user.setEmail("user1@mail.ru");
        user.setLogin("user");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userRepository.createUser(user);

        User user1 = new User();
        user1.setEmail("user1@mail.ru");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        assertEquals("User One", user1.getName(), "Имя пользователя не совпадает.");
        assertEquals("user1", user1.getLogin(), "Логин пользователя не обновился.");
    }

    @Test
    void isValidateCreatedUser() {
        User user = new User();
        user.setEmail("null@ya.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1995, 8, 22));

        User createdUser = userRepository.createUser(user);
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("null@ya.ru");
        assertThat(createdUser.getLogin()).isEqualTo("login");
        assertThat(createdUser.getName()).isEqualTo("name");
        assertThat(createdUser.getBirthday()).isEqualTo(LocalDate.of(1995, 8, 22));
    }

    @Test
    void isValidateGetUserById() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userRepository.createUser(user);
        User foundUser = userRepository.getUserById(createdUser.getId());

        assertThat(foundUser.getId()).isEqualTo(createdUser.getId());
        assertThat(foundUser.getEmail()).isEqualTo("user@mail.ru");
        assertThat(foundUser.getLogin()).isEqualTo("user");
        assertThat(foundUser.getName()).isEqualTo("User");
        assertThat(foundUser.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void isValidateGetAllUsers() {
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
        Collection<User> allUsers = userRepository.getAllUsers();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers)
                .extracting(User::getName)
                .containsExactlyInAnyOrder("name", "name1");
    }

    @Test
    void isValidateGetCommonFriends() {
        User user1 = new User();
        user1.setEmail("user1@mail.ru");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser1 = userRepository.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@mail.ru");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        User createdUser2 = userRepository.createUser(user2);

        User user3 = new User();
        user3.setEmail("user3@mail.ru");
        user3.setLogin("user3");
        user3.setName("User Three");
        user3.setBirthday(LocalDate.of(1992, 1, 1));
        User createdUser3 = userRepository.createUser(user3);

        User user4 = new User();
        user4.setEmail("user4@mail.ru");
        user4.setLogin("user4");
        user4.setName("User Four");
        user4.setBirthday(LocalDate.of(1993, 1, 1));
        User createdUser4 = userRepository.createUser(user4);

        friendshipRepository.addFriend(createdUser1.getId(), createdUser3.getId());
        friendshipRepository.addFriend(createdUser1.getId(), createdUser4.getId());
        friendshipRepository.addFriend(createdUser2.getId(), createdUser3.getId());
        friendshipRepository.addFriend(createdUser2.getId(), createdUser4.getId());

        Collection<User> commonFriends = userRepository.getCommonFriends(
                createdUser1.getId(), createdUser2.getId());
        assertThat(commonFriends).hasSize(2);
        assertThat(commonFriends)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(createdUser3.getId(), createdUser4.getId());
    }

    @Test
    void isValidateGetFriends() {
        User user1 = new User();
        user1.setEmail("user1@mail.ru");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser1 = userRepository.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@mail.ru");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        User createdUser2 = userRepository.createUser(user2);

        User user3 = new User();
        user3.setEmail("user3@mail.ru");
        user3.setLogin("user3");
        user3.setName("User Three");
        user3.setBirthday(LocalDate.of(1992, 1, 1));
        User createdUser3 = userRepository.createUser(user3);

        friendshipRepository.addFriend(createdUser1.getId(), createdUser2.getId());
        friendshipRepository.addFriend(createdUser1.getId(), createdUser3.getId());

        Collection<User> friends = userRepository.getFriends(createdUser1.getId());
        assertThat(friends).hasSize(2);
        assertThat(friends)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(createdUser2.getId(), createdUser3.getId());
    }
}
