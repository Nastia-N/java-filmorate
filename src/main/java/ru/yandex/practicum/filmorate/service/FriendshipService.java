package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    public void addFriend(long friendId, long userId) {
        friendshipRepository.addFriend(friendId, userId);
    }

    public void removeFriend(long userId, long friendId) {
        friendshipRepository.removeFriend(userId, friendId);
    }
}
