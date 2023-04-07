package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    public static Long idFilm = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {
        if (film.getId() == null || film.getId() == 0) {
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

    public Film put(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IncorrectParameterException("Фильм с id " + film.getId() + " еще не добавлен.");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные фильма с id " + film.getId());
        return film;
    }
}
