package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping
    public Collection<FilmGenre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public FilmGenre getGenreById(@PathVariable int id) {
        return genreService.getGenreById(id);
    }
}
