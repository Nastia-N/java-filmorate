package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NewFilm;

import java.util.Collection;
import java.util.List;

public interface FilmRepository {

    Collection<Film> getAllFilms();

    Film createFilm(NewFilm film);

    Film updateFilm(Film newFilm);

    Film getFilmById(long id);

    List<Film> getPopularFilms(int count);

    Film getFilmWithGenres(long id);
}
