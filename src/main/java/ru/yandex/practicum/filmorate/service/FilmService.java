package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NewFilm;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private UserService userService;

    public Collection<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    public Film createFilm(NewFilm film) {
        validateFilm(film);
        return filmRepository.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        validateFilm(newFilm);
        return filmRepository.updateFilm(newFilm);
    }

    public Film getFilmById(Long id) {
        return filmRepository.getFilmWithGenres(id);
    }

    public List<Film> getPopularFilms(int count) {
        return filmRepository.getPopularFilms(count);
    }

    public static void validateFilm(NewFilm film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза должна быть указана (начиная с 28 декабря 1895 года)");
        }
    }
}
