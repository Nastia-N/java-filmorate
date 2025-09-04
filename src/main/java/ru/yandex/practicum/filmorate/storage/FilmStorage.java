package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NewFilm;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film createFilm(NewFilm film);

    Film updateFilm(Film newFilm);

    Film getFilmById(Long id);

}
