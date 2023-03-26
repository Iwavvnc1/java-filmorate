package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final LocalDate initialReleaseDate = LocalDate.of(1895, 12, 28);
    static int idFilm= 0;
    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validationFilm(film);
        if (films.containsKey(film.getName())) {
            throw new ValidationException("Фильма с названием " +
                    film.getName() + " уже добавлен.");
        }
        film.toBuilder().id(++idFilm);
        films.put(film.getName(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        validationFilm(film);
        films.put(film.getName(), film);
        return film;
    }

    private void validationFilm(Film film) {
        int maxDescriptionSize = 200;

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > maxDescriptionSize) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(initialReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше чем 28.12.1895.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма неможет быть меньше или равна 0.");
        }
    }
    /*Для FilmController:
добавление фильма;
обновление фильма;
получение всех фильмов.*/
}
