package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLikeRepository implements LikeRepository {
    private final JdbcTemplate jdbc;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public void addLike(long filmId, long userId) {
        filmRepository.getFilmById(filmId);
        userRepository.getUserById(userId);
        String query = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbc.update(query, filmId, userId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        filmRepository.getFilmById(filmId);
        userRepository.getUserById(userId);
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbc.update(query, filmId, userId);
    }
}
