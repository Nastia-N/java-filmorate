package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId).addFriend(friendId);
        userStorage.getUserById(friendId).addFriend(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId).removeFriend(friendId);
        userStorage.getUserById(friendId).removeFriend(userId);
    }

    public Set<Long> getCommonFriends(Long id1, Long id2) {
        Set<Long> u1 = userStorage.getUserById(id1).getFriends();
        Set<Long> u2 = userStorage.getUserById(id2).getFriends();
        Set<Long> copy = new HashSet<>(u1);
        copy.retainAll(u2);
        return copy;
    }

    public void addLikeToFilm(Long userId, Long filmId) {
        userStorage.getUserById(userId).addLikeToFilm(filmId);
    }

    public void removeLikeToFilm(Long userId, Long filmId) {
        userStorage.getUserById(userId).removeLikeToFilm(filmId);
    }
}
