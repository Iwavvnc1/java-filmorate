package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends LikesStorage {
    Collection<Film> getAll();

    Film create(Film film);

    Film put(Film film);

    Film getFilm(Long filmId);
}
