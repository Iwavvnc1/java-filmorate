package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    void deleteAllGenresById(Long filmId);

    Genre getGenreById(Long genreId);

    List<Genre> getAllGenres();
}
