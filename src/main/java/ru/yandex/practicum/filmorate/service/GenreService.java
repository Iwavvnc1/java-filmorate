package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenreById(Long id) {
        Genre genre = genreDbStorage.getGenreById(id);
        if (genre == null) {
            throw new IncorrectParameterException("Жанр не найден!");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }
}

