package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    FilmStorage filmStorage;
    public UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film put(Film film) {
        validation(film.getId());
        return filmStorage.put(film);
    }

    public Film addLike(Long filmId, Long userId) {
        validation(filmId);
        validation(userId);
        return filmStorage.addLike(filmId, userId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        validation(filmId);
        validation(userId);
        return filmStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getPopularFilm(Integer count) {
        return filmStorage.getPopularFilm(count);
    }

    public Film getFilm(Long filmId) {
        return filmStorage.getFilm(filmId);
    }
    private void validation(Long filmId) {
        filmStorage.getFilm(filmId);
    }
}
