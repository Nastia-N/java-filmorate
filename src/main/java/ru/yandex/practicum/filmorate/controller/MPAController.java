package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MPAController {
    @Autowired
    private MPAService mpaService;

    @GetMapping
    public Collection<MpaRating> getAllRating() {
        return mpaService.getAllRating();
    }

    @GetMapping("/{id}")
    public MpaRating getRatingById(@PathVariable int id) {
        return mpaService.getRatingById(id);
    }
}
