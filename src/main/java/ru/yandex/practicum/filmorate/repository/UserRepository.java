package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.NewUser;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserRepository {

    Collection<User> getAllUsers();

    User createUser(NewUser user);

    User updateUser(User newUser);

    User getUserById(long id);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long userId, long otherId);

}
