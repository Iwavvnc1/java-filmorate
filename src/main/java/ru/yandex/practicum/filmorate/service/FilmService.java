package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

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
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }

    public Film addLike(Long filmId, Long userId) {
        if (filmStorage.findAll().stream().anyMatch(films -> films.getId().equals(filmId))) {
            if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
                Film film = filmStorage.findAll().stream()
                        .filter(films -> films.getId().equals(filmId))
                        .findFirst().get();
                if (film.getLikes().stream().noneMatch(usersId -> usersId.equals(userId))) {
                    film.getLikes().add(userId);
                    return film;
                } else {
                    throw new IncorrectParameterException("Пользователь с id:" + userId
                            + "уже поставил лайк фильму с id:" + filmId);
                }
            } else {
                throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
            }
        } else {
            throw new IncorrectParameterException("Фильм с id:" + filmId + " не найден");
        }
    }

    public Film deleteLike(Long filmId, Long userId) {
        if (filmStorage.findAll().stream().anyMatch(films -> films.getId().equals(filmId))) {
            if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
                Film film = filmStorage.findAll().stream()
                        .filter(films -> films.getId().equals(filmId))
                        .findFirst().get();
                if (film.getLikes().stream().anyMatch(usersId -> usersId.equals(userId))) {
                    film.getLikes().remove(userId);
                    return film;
                } else {
                    throw new IncorrectParameterException("Пользователь с id:" + userId
                            + "еще не поставил лайк фильму с id:" + filmId);
                }
            } else {
                throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
            }
        } else {
            throw new IncorrectParameterException("Фильм с id:" + filmId + " не найден");
        }
    }

    public Collection<Film> getPopularFilm(Integer count) {
        return findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }

    public Film getFilm(Long filmId) {
        if (filmStorage.findAll().stream().anyMatch(films -> films.getId().equals(filmId))) {
            Film film = filmStorage.findAll().stream()
                    .filter(films -> films.getId().equals(filmId))
                    .findFirst().get();
            return film;
        } else {
            throw new IncorrectParameterException("Фильм с id:" + filmId + " не найден");
        }
    }
}
