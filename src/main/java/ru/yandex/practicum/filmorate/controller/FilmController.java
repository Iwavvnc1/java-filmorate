package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmService.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@Valid @RequestBody @PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@Valid @RequestBody @PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@Valid @RequestBody
                                           @RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilm(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@Valid @RequestBody @PathVariable("id") Long filmId) {
        return filmService.getFilm(filmId);
    }
}
