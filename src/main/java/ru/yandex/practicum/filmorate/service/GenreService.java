package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;

    public Collection<FilmGenre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public FilmGenre getGenreById(int id) {
        return genreRepository.getGenreById(id);
    }
}
