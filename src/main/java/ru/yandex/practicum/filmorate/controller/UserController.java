package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.NewUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid NewUser user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User newUser) {
        return userStorage.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}/request")
    public void addFriendRequest(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriendRequest(id, friendId);
    }

    @PutMapping("/{id}/friends/{friendId}/confirm")
    public void addFriendConfirm(@PathVariable Long id, @PathVariable Long friendId) {
        userService.confirmFriend(id, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        return userService.getCommonFriends(userId, otherId).stream().map(userStorage::getUserById).toList();
    }

    @GetMapping("/{userId}/friends")
    public Map<User, Friendship> getFriends(@PathVariable Long userId) {
        return userStorage.getUserById(userId).getFriends().entrySet().stream()
                .collect(Collectors.toMap(e -> userStorage.getUserById(e.getKey()), Map.Entry::getValue));
    }
}