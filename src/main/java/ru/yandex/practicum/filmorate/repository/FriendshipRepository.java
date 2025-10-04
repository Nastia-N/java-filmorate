package ru.yandex.practicum.filmorate.repository;

public interface FriendshipRepository {

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

}
