package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.MPARepository;

import java.util.Collection;

@Service
public class MPAService {
    @Autowired
    private MPARepository mpaRepository;

    public Collection<MpaRating> getAllRating() {
        return mpaRepository.getAllRating();
    }

    public MpaRating getRatingById(int id) {
        return mpaRepository.getRatingById(id);
    }
}
