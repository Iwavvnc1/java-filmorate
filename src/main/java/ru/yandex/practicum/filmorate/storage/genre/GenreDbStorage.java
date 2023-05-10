package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteAllGenresById(Long filmId) {
        String sglQuery = "DELETE FROM film_genres WHERE genre_id = ?";
        jdbcTemplate.update(sglQuery, filmId);
    }

    @Override
    public Genre getGenreById(Long genreId) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, genreId);
        if (srs.next()) {
            return new Genre(genreId, srs.getString("genre_name"));
        }
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sqlQuery = "SELECT * FROM genres ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (srs.next()) {
            genres.add(new Genre(srs.getLong("genre_id"), srs.getString("genre_name")));
        }
        return genres;
    }
}
