package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserService userService;

    public void addLike(Long userId, Long filmId) {
        filmStorage.getFilmById(filmId).addLike(userId);
        userService.addLikeToFilm(userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        filmStorage.getFilmById(filmId).removeLike(userId);
        userService.removeLikeToFilm(userId, filmId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        films.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        return films.stream().limit(count).collect(Collectors.toList());
    }
}
