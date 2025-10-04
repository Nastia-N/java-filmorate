package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface MPARepository {

    Collection<MpaRating> getAllRating();

    MpaRating getRatingById(int id);

}
