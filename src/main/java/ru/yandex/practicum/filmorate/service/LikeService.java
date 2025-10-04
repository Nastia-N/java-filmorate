package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.LikeRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    public void addLike(long filmId, long userId) {
        likeRepository.addLike(filmId, userId);
    }

    public void removeLike(long userId, long filmId) {
        likeRepository.removeLike(userId, filmId);
    }
}
