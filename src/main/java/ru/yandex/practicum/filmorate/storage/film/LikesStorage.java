package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface LikesStorage {
    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    Collection<Film> getPopularFilm(Integer count);
}
