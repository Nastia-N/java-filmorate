package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public void addFriendRequest(Long userId, Long friendId) {
        userStorage.getUserById(userId).sendFriendRequest(friendId);
        userStorage.getUserById(friendId).waitFriendRequest(userId);
    }

    public void confirmFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId).confirmFriend(friendId);
        userStorage.getUserById(friendId).updateFriend(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId).removeFriend(friendId);
        userStorage.getUserById(friendId).removeFriend(userId);
    }

    public Set<Long> getCommonFriends(Long id1, Long id2) {
        Set<Long> u1 = userStorage.getUserById(id1).getFriends().entrySet().stream()
                .filter(entry -> {
                    return entry.getValue().equals(Friendship.CONFIRMED);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Set<Long> u2 = userStorage.getUserById(id2).getFriends().entrySet().stream()
                .filter(entry -> {
                    return entry.getValue().equals(Friendship.CONFIRMED);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
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
