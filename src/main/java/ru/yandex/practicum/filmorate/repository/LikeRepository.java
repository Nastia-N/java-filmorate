package ru.yandex.practicum.filmorate.repository;

public interface LikeRepository {

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

}
