package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GenreRepository {

    Collection<FilmGenre> getAllGenres();

    FilmGenre getGenreById(int id);

    void saveGenres(Set<Integer> genreIds, long filmId);

    List<FilmGenre> getGenresByFilmId(long filmId);
}
