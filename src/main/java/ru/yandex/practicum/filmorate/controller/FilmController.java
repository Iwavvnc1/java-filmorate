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
    public static int idFilm = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validationFilm(film);
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
    public Film put(@RequestBody Film film) {
        validationFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " еще не добавлен.");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные фильма с id " + film.getId());
        return film;
    }

    private void validationFilm(Film film) {
        int maxDescriptionSize = 200;

        if (film.getId() == 0 && film.getDescription() == null && film.getReleaseDate() == null
        && film.getName() == null && film.getDuration() == 0 ) {
            log.warn("Все данные фильма пусты.");
            throw new ValidationException("Все данные фильма пусты.");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Не верное название фильма");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()
                || film.getDescription().length() > maxDescriptionSize) {
            log.warn("Не верное описание фильма.");
            throw new ValidationException("Описания не может быть пустым или больше 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(initialReleaseDate)) {
            log.warn("Не верная дата релиза фильма.");
            throw new ValidationException("Дата релиза не может быть пустой или раньше чем 28.12.1895.");
        }
        if (film.getDuration() <= 0) {
            log.warn("Не верная продолжительность фильма.");
            throw new ValidationException("Продолжительность фильма неможет быть меньше или равна 0.");
        }
    }
}
