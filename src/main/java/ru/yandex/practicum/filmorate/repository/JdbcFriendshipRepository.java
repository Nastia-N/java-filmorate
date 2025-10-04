package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcFriendshipRepository implements FriendshipRepository {
    private final JdbcTemplate jdbc;
    private final JdbcUserRepository userRepository;

    @Override
    public void addFriend(long userId, long friendId) {
        userRepository.getUserById(userId);
        userRepository.getUserById(friendId);
        String query = "INSERT INTO friendships (friend_id, user_id) VALUES (?, ?)";
        jdbc.update(query, friendId, userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userRepository.getUserById(userId);
        userRepository.getUserById(friendId);
        String query = "DELETE FROM friendships WHERE friend_id = ? AND user_id = ?";
        jdbc.update(query, friendId, userId);
    }
}
