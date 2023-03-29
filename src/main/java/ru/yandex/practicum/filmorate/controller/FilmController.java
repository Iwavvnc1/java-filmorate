package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    public static int idFilm = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getId() == 0) {
            film = film.toBuilder().id(idFilm++).build();
            while (films.containsKey(film.getId())) {
                film = film.toBuilder().id(idFilm++).build();
            }
        }
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с id " + film.getId() + " уже добавлен.");
        }
        films.put(film.getId(), film);
        log.debug("Добавлен фильм с id " + film.getId());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " еще не добавлен.");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные фильма с id " + film.getId());
        return film;
    }
}
