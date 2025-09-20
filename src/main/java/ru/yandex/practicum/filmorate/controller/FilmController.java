package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.NewFilm;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmService filmService;


    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid NewFilm film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film newFilm) {
        return filmStorage.updateFilm(newFilm);
        }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.removeLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}
