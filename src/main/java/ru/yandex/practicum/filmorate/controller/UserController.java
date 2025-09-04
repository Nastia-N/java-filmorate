package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.NewUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

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

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
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
    public List<User> getFriends(@PathVariable Long userId) {
        return userStorage.getUserById(userId).getFriends().stream().map(userStorage::getUserById).toList();
    }
}