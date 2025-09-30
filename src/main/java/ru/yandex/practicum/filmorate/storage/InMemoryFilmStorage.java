package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NewFilm;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film createFilm(NewFilm film) {
        validateFilm(film);
        Film f = new Film(getNextId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getGenre(), film.getRating());
        films.put(f.getId(), f);
        log.info("Фильм с id {} создан", f.getId());
        return f;
    }

    public Film updateFilm(Film newFilm) {
        validateFilm(newFilm);
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм с id {} обновлён", newFilm.getId());
            return films.get(newFilm.getId());
        }
        throw new NotFoundException("Фильма с id " + newFilm.getId() + " не существует");
    }

    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Фильма с id " + id + " не существует");
    }

    private long getNextId() {
        return nextId++;
    }

    public static void validateFilm(NewFilm film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза должна быть указана (начиная с 28 декабря 1895 года)");
        }
    }
}
