package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.NewUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User createUser(NewUser user) {
        validateUser(user);
        return userRepository.createUser(user);
    }

    public User updateUser(User newUser) {
        validateUser(newUser);
        return userRepository.updateUser(newUser);
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public Collection<User> getFriends(long id) {
        return userRepository.getFriends(id);
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        return userRepository.getCommonFriends(userId, otherId);
    }

    public static void validateUser(NewUser user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public Set<Long> getCommonFriendsOld(Long id1, Long id2) {
        Set<Long> u1 = userRepository.getUserById(id1).getFriends().entrySet().stream()
                .filter(entry -> {
                    return entry.getValue().equals(Friendship.CONFIRMED);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Set<Long> u2 = userRepository.getUserById(id2).getFriends().entrySet().stream()
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
        userRepository.getUserById(userId).addLikeToFilm(filmId);
    }

    public void removeLikeToFilm(Long userId, Long filmId) {
        userRepository.getUserById(userId).removeLikeToFilm(filmId);
    }

    public void addFriendRequest(Long userId, Long friendId) {
        userRepository.getUserById(userId).sendFriendRequest(friendId);
        userRepository.getUserById(friendId).waitFriendRequest(userId);
    }

    public void confirmFriend(Long userId, Long friendId) {
        userRepository.getUserById(userId).confirmFriend(friendId);
        userRepository.getUserById(friendId).updateFriend(userId);
    }
}
